package io.github.alxiw.openweatherforecast.di

import dagger.Module
import dagger.Provides
import io.github.alxiw.openweatherforecast.api.OpenWeatherMapService
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.db.WeatherLocalCache
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideGithubRepository(service: OpenWeatherMapService, cache: WeatherLocalCache): WeatherRepository {
        return WeatherRepository(service, cache)
    }

}