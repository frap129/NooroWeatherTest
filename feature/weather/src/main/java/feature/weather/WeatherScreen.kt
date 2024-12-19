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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import core.data.weather.model.CurrentWeatherAtLocation
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
                SearchResults(results.value)
            }

            is WeatherScreenState.Location -> {}
        }
    }
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

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
        keyboardActions = KeyboardActions(onSearch = { onSearch(searchQuery) })
    )
}

@Composable
fun SearchResults(searchResults: List<CurrentWeatherAtLocation>) {
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = true,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(searchResults) { item ->
            SearchItem(item)
        }
    }
}

@Composable
fun SearchItem(weatherAtLocation: CurrentWeatherAtLocation) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(16.dp)
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
                    text = weatherAtLocation.location.name,
                    style = TextStyle(
                        fontWeight = FontWeight.W600,
                        fontSize = 24.sp
                    )
                )
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = weatherAtLocation.current.temp_c.roundToInt().toString(),
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
                model = "https:${weatherAtLocation.current.condition.icon}",
                contentScale = ContentScale.Fit,
                contentDescription = weatherAtLocation.current.condition.text,
                modifier = Modifier.size(90.dp)
            )
        }
    }
}
