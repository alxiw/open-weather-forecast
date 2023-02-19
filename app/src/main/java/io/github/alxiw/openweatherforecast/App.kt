package io.github.alxiw.openweatherforecast

import android.app.Application
import io.github.alxiw.openweatherforecast.di.appModule
import io.github.alxiw.openweatherforecast.di.databaseModule
import io.github.alxiw.openweatherforecast.di.networkModule
import io.github.alxiw.openweatherforecast.data.WeatherWorker
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, networkModule, databaseModule))
        }
        initRealm(this)
        initDebug()
    }

    private fun initRealm(app: App) {
        Realm.init(app)
        val realmConfig = RealmConfiguration.Builder()
            .name(DB_NAME)
            .schemaVersion(DB_VERSION)
            .build()
        Realm.setDefaultConfiguration(realmConfig)
        Timber.i("Weather Worker: init with %d hours interval", DB_CLEAR_INTERVAL_HOURS)
        WeatherWorker.init(applicationContext, DB_CLEAR_INTERVAL_HOURS)
    }

    private fun initDebug() {
        if (!BuildConfig.DEBUG) {
            return
        }
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        private const val DB_NAME = "openweatherforecast.realm"
        private const val DB_VERSION = 0L
        private const val DB_CLEAR_INTERVAL_HOURS = 12L
    }
}
