package core.data.weather

import core.data.BuildConfig
import core.data.weather.model.CurrentWeatherAtLocation
import core.data.weather.model.Location
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("current.json")
    suspend fun getCurrentWeatherAtLocation(
        @Query("key") key: String = BuildConfig.weatherApiKey,
        @Query("q") location: String,
        @Query("aqi") includeAirQuality: Boolean = false
    ): CurrentWeatherAtLocation

    @GET("search.json")
    suspend fun queryLocations(@Query("key") key: String = BuildConfig.weatherApiKey, @Query("q") query: String): List<Location>
}
