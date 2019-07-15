package io.github.alxiw.openweatherforecast.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.ui.ViewModelFactory
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(repository: WeatherRepository): ViewModelProvider.Factory {
        return ViewModelFactory(repository)
    }

}