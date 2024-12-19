package core.data.prefs

import core.data.weather.model.Location
import kotlinx.coroutines.flow.StateFlow

interface PreferencesRepository {
    val savedLocation: StateFlow<Location?>

    fun setLocation(location: Location)
}
