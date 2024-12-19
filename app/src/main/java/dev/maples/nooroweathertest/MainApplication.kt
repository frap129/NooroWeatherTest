package dev.maples.nooroweathertest

import android.app.Application
import android.content.Context
import androidx.room.Room
import core.data.RetrofitClient
import core.data.prefs.PreferencesRepository
import core.data.prefs.PreferencesRepositoryImpl
import core.data.room.AppDatabase
import core.data.room.dao.LocationDao
import core.data.weather.WeatherAPIService
import core.data.weather.WeatherRepository
import core.data.weather.WeatherRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MainApplication : Application() {
    private val repoCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    private val dependencies = module {
        single<MainApplication> { this@MainApplication }
        single<WeatherAPIService> { RetrofitClient.weatherAPIService }
        single<LocationDao> { database.getLocationDao() }
        single<WeatherRepository> { WeatherRepositoryImpl(get(), repoCoroutineScope) }
        single<PreferencesRepository> { PreferencesRepositoryImpl(get(), repoCoroutineScope) }
    }

    override fun attachBaseContext(base: Context) {
        startKoin {
            androidContext(base)
            androidLogger()
            modules(dependencies)
        }
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        // Set up Timber
        when (BuildConfig.DEBUG) {
            true -> Timber.plant(Timber.DebugTree())
            false -> Timber.plant(ReleaseTree)
        }
    }

    private object ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // TODO: Configure reporting for release builds
        }
    }
}
