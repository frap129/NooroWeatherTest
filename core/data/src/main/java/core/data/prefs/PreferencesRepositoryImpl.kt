package core.data.prefs

import core.data.room.dao.LocationDao
import core.data.weather.model.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PreferencesRepositoryImpl(private val locationDao: LocationDao, private val coroutineScope: CoroutineScope) : PreferencesRepository {
    override val savedLocation: StateFlow<Location?>
        get() = locationDao.getLocation().stateIn(scope = coroutineScope, started = SharingStarted.WhileSubscribed(), initialValue = null)

    override fun setLocation(location: Location) {
        coroutineScope.launch {
            locationDao.updateLocation(location)
        }
    }
}
