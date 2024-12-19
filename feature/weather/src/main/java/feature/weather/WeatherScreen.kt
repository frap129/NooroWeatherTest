package feature.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import core.data.weather.model.CurrentWeather
import core.data.weather.model.CurrentWeatherAtLocation
import core.data.weather.model.Location
import core.ui.Destination
import core.ui.theme.OnCardBackgroundLight
import kotlin.math.roundToInt
import org.koin.androidx.compose.koinViewModel

val weatherDestination = Destination(
    route = "weather",
    content = { navController, _ ->
        WeatherScreen(navController)
    }
)

@Composable
fun WeatherScreen(navController: NavController, viewModel: WeatherViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(onSearch = { viewModel.searchLocations(it) })
        when (uiState) {
            WeatherScreenState.Loading -> {}

            WeatherScreenState.Error -> {}

            WeatherScreenState.NoLocation -> {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "No City Selected",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please Search For A City",
                    style = TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            is WeatherScreenState.Search -> {
                val results = (uiState as WeatherScreenState.Search).searchResults.collectAsState()
                SearchResults(results.value) {
                    viewModel.selectLocation(it)
                }
            }

            is WeatherScreenState.Location -> {
                Location((uiState as WeatherScreenState.Location).currentWeatherAtLocation)
            }
        }
    }
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Spacer(modifier = Modifier.height(32.dp))
    TextField(
        singleLine = true,
        value = searchQuery,
        placeholder = {
            Text(
                text = "Search Location",
                color = OnCardBackgroundLight
            )
        },
        onValueChange = { searchQuery = it },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        ),
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                    focusManager.clearFocus()
                    onSearch(searchQuery)
                },
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = OnCardBackgroundLight
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onSearch(searchQuery)
        })
    )
}

@Composable
fun SearchResults(searchResults: List<Pair<Location, CurrentWeather>>, onItemClicked: (Location) -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = true,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(searchResults) { item ->
            SearchItem(item.first, item.second, onItemClicked)
        }
    }
}

@Composable
fun SearchItem(location: Location, weather: CurrentWeather, onClick: (Location) -> Unit) {
    val focusManager = LocalFocusManager.current

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                focusManager.clearFocus()
                onClick(location)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = location.name,
                    style = TextStyle(
                        fontWeight = FontWeight.W600,
                        fontSize = 24.sp
                    )
                )
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = weather.temp_c.roundToInt().toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.W500,
                            fontSize = 48.sp
                        )
                    )
                    Image(
                        modifier = Modifier.padding(top = 8.dp),
                        painter = painterResource(R.drawable.ellipse),
                        contentDescription = "degrees"
                    )
                }
            }

            AsyncImage(
                model = "https:${weather.condition.icon}",
                contentDescription = weather.condition.text,
                modifier = Modifier.size(90.dp)
            )
        }
    }
}

@Composable
fun Location(weatherAtLocation: CurrentWeatherAtLocation) {
    val weather = weatherAtLocation.current
    val location = weatherAtLocation.location

    Spacer(modifier = Modifier.height(64.dp))
    AsyncImage(
        model = "https:${weather.condition.icon}",
        contentDescription = weather.condition.text,
        modifier = Modifier.size(90.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = location.name,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.vector),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }

    Text(
        text = weather.temp_c.roundToInt().toString(),
        style = TextStyle(
            fontSize = 64.sp,
            fontWeight = FontWeight.Light
        )
    )
    Spacer(modifier = Modifier.height(24.dp))
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Humidity", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Light))
                Text(text = "${weather.humidity}%", style = TextStyle(fontWeight = FontWeight.Normal))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "UV", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Light))
                Text(text = weather.uv.toString(), style = TextStyle(fontWeight = FontWeight.Normal))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Feels Like", style = TextStyle(color = Color.Gray, fontWeight = FontWeight.Light))
                Text(text = weather.feelslike_c.toString(), style = TextStyle(fontWeight = FontWeight.Normal))
            }
        }
    }
}
