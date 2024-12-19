package feature.weather

import androidx.compose.ui.util.fastMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.prefs.PreferencesRepository
import core.data.weather.WeatherRepository
import core.data.weather.model.CurrentWeatherAtLocation
import core.data.weather.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class WeatherScreenState {
    data object Loading : WeatherScreenState()
    data object Error : WeatherScreenState()
    data object NoLocation : WeatherScreenState()
    data class Search(val searchResults: StateFlow<List<CurrentWeatherAtLocation>>) : WeatherScreenState()
    data class Location(val currentWeatherAtLocation: CurrentWeatherAtLocation) : WeatherScreenState()
}

class WeatherViewModel(private val preferencesRepo: PreferencesRepository, private val weatherRepo: WeatherRepository) : ViewModel() {
    private val savedLocation: StateFlow<Location?> = preferencesRepo.savedLocation
    private val _uiState: MutableStateFlow<WeatherScreenState> = MutableStateFlow(WeatherScreenState.Loading)
    val uiState: StateFlow<WeatherScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savedLocation.onEach { location: Location? ->
                if (location == null) {
                    _uiState.value = WeatherScreenState.NoLocation
                } else {
                    _uiState.value = WeatherScreenState.Location(weatherRepo.getCurrentWeatherAtLocation(location))
                }
            }.collect()
        }
    }

    fun searchLocations(query: String) {
        _uiState.value = WeatherScreenState.Search(
            weatherRepo.searchLocations(query).map { locations: List<Location> ->
                locations.fastMap { location ->
                    weatherRepo.getCurrentWeatherAtLocation(location)
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
        )
    }

    fun selectLocation(location: Location) = preferencesRepo.setLocation(location)
}
