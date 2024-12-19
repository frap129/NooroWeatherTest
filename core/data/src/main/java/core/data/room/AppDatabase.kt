package core.data.room
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import core.data.room.dao.LocationDao
import core.data.weather.model.Location

@Database(
    entities = [
        Location::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLocationDao(): LocationDao
}
