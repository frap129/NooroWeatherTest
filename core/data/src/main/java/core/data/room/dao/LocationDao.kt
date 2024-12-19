package core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.data.weather.model.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location LIMIT 1")
    fun getLocation(): Flow<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLocation(location: Location)
}
