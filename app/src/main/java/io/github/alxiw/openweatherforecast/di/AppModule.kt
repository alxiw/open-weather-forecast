package io.github.alxiw.openweatherforecast.di

import android.content.Context
import android.content.SharedPreferences
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.domain.LoadForecastUseCase
import io.github.alxiw.openweatherforecast.presentation.ForecastViewModel
import io.github.alxiw.openweatherforecast.presentation.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val PREF_FILE_NAME = "weather_prefs"

val appModule = module {

    factory { WeatherRepository(get(), get()) }
    factory { LoadForecastUseCase(get()) }
    single { (get() as Context).getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE) as SharedPreferences }
    viewModel { ForecastViewModel(get(), get()) }
    viewModel { DetailsViewModel(get()) }
}
