package core.data.weather

import core.data.weather.model.CurrentWeatherAtLocation
import core.data.weather.model.Location
import kotlinx.coroutines.flow.StateFlow

interface WeatherRepository {
    suspend fun getCurrentWeatherAtLocation(location: Location): CurrentWeatherAtLocation
    fun searchLocations(query: String): StateFlow<List<Location>>
}
