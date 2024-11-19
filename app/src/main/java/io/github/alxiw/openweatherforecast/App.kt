package io.github.alxiw.openweatherforecast

import android.app.Application
import android.util.Log
import io.github.alxiw.openweatherforecast.di.appModule
import io.github.alxiw.openweatherforecast.di.databaseModule
import io.github.alxiw.openweatherforecast.di.networkModule
import io.github.alxiw.openweatherforecast.data.WeatherWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, networkModule, databaseModule))
        }
        initWorker()
        initDebug()
    }

    private fun initWorker() {
        Log.i("HELLO", "Weather Worker: init with $DB_CLEAR_INTERVAL_HOURS hours interval")
        WeatherWorker.init(applicationContext, DB_CLEAR_INTERVAL_HOURS)
    }

    private fun initDebug() {
        if (!BuildConfig.DEBUG) {
            return
        }
    }

    companion object {
        private const val DB_CLEAR_INTERVAL_HOURS = 12L
    }
}
