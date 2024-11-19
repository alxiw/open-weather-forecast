package io.github.alxiw.openweatherforecast.di

import io.github.alxiw.openweatherforecast.data.db.WeatherLocalCache
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val DB_NAME = "openweatherforecast.realm"
private const val DB_VERSION = 0L

val databaseModule = module {

    factory { Executors.newSingleThreadExecutor() as Executor }
    factory {
        val config = with(RealmConfiguration.Builder(setOf(Forecast::class))) {
            name(DB_NAME)
            schemaVersion(DB_VERSION)
            build()
        }
        Realm.open(config) as Realm
    }
    factory { WeatherLocalCache(get()) as WeatherLocalCache }
}
