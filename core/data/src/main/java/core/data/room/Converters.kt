package core.data.room

import androidx.room.TypeConverter
import core.data.weather.model.Location
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromLocationToString(location: Location): String = Json.encodeToString(location)

    @TypeConverter
    fun fromStringToLocation(location: String): Location = Json.decodeFromString(location)
}
