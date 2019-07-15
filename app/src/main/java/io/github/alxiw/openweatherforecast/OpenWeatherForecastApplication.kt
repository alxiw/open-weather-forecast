package io.github.alxiw.openweatherforecast

import android.app.Application
import io.github.alxiw.openweatherforecast.di.AppComponent
import io.github.alxiw.openweatherforecast.di.AppModule
import io.github.alxiw.openweatherforecast.di.DaggerAppComponent
import io.realm.Realm
import io.realm.RealmConfiguration

class OpenWeatherForecastApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = initDagger(this)
        initRealm(this)
    }

    private fun initDagger(app: OpenWeatherForecastApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()

    private fun initRealm(app: OpenWeatherForecastApplication) {
        Realm.init(app)
        val realmConfig = RealmConfiguration.Builder()
            .name("weather.realm")
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

}