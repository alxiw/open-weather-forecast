package io.github.alxiw.openweatherforecast.di

import io.github.alxiw.openweatherforecast.data.cache.WeatherCache
import io.github.alxiw.openweatherforecast.data.cache.model.CityRealm
import io.github.alxiw.openweatherforecast.data.cache.model.ForecastRealm
import io.github.alxiw.openweatherforecast.data.cache.model.QueryRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

private const val DB_NAME = "openweatherforecast.realm"
private const val DB_VERSION = 0L

val databaseModule = module {

    factory {
        val set = setOf(QueryRealm::class, CityRealm::class, ForecastRealm::class)
        val config = with(RealmConfiguration.Builder(set)) {
            name(DB_NAME)
            schemaVersion(DB_VERSION)
            build()
        }
        Realm.open(config) as Realm
    }
    factory { WeatherCache(get()) as WeatherCache }
}
