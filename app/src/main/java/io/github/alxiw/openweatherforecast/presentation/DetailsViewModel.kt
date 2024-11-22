package io.github.alxiw.openweatherforecast.presentation

import androidx.lifecycle.ViewModel
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.data.model.Forecast

class DetailsViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    fun getForecast(key: String): Forecast? {
        return repository.getByKey(key)
    }
}
