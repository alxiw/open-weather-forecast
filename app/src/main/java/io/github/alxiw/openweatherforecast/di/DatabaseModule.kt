package io.github.alxiw.openweatherforecast.di

import io.github.alxiw.openweatherforecast.db.WeatherLocalCache
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val databaseModule = module {

    factory { Executors.newSingleThreadExecutor() as Executor }
    factory { WeatherLocalCache(get()) as WeatherLocalCache }
}
