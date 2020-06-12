package io.github.alxiw.openweatherforecast.di

import android.content.Context
import android.content.SharedPreferences
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.ui.forecasts.ForecastsViewModel
import io.github.alxiw.openweatherforecast.ui.details.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val PREF_FILE_NAME = "weather_prefs"

val appModule = module {

    single { (get() as Context).getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE) as SharedPreferences }
    factory { WeatherRepository(get(), get()) }
    viewModel { ForecastsViewModel(get(), get()) }
    viewModel { DetailsViewModel(get()) }
}
