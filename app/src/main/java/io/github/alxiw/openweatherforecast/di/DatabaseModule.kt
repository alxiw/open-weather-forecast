package io.github.alxiw.openweatherforecast.di

import dagger.Module
import dagger.Provides
import io.github.alxiw.openweatherforecast.db.WeatherLocalCache
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideCache(): WeatherLocalCache {
        return WeatherLocalCache(Executors.newSingleThreadExecutor())
    }

}