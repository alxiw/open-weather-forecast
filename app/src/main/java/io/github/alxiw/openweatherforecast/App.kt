package io.github.alxiw.openweatherforecast

import android.app.Application
import io.github.alxiw.openweatherforecast.di.appModule
import io.github.alxiw.openweatherforecast.di.databaseModule
import io.github.alxiw.openweatherforecast.di.networkModule
import io.realm.Realm
import io.realm.RealmConfiguration
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
        initRealm(this)
    }

    private fun initRealm(app: App) {
        Realm.init(app)
        val realmConfig = RealmConfiguration.Builder()
            .name(DB_NAME)
            .schemaVersion(DB_VERSION)
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    companion object {
        private const val DB_NAME = "openweatherforecast.realm"
        private const val DB_VERSION = 0L
    }
}
