package core.data.weather

import core.data.weather.model.CurrentWeather
import core.data.weather.model.CurrentWeatherAtLocation
import core.data.weather.model.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class WeatherRepositoryImpl(private val weatherAPIService: WeatherAPIService, private val coroutineScope: CoroutineScope) :
    WeatherRepository {

    override suspend fun getCurrentWeatherAtLocation(location: Location): CurrentWeatherAtLocation = weatherAPIService
            .getCurrentWeatherAtLocation(location = "${location.lat},${location.lon}")

    override fun searchLocations(query: String) = flow {
        emit(weatherAPIService.queryLocations(query = query))
    }.stateIn(scope = coroutineScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList())
}
