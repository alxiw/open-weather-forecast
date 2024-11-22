package io.github.alxiw.openweatherforecast.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import io.github.alxiw.openweatherforecast.domain.type.Failure
import io.github.alxiw.openweatherforecast.domain.type.HandleOnce
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.domain.LoadForecastUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ForecastViewModel(
    private val loadForecastUseCase: LoadForecastUseCase,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val forecastDataMutableFlow = MutableSharedFlow<List<Forecast>>(1, 0, BufferOverflow.DROP_OLDEST)
    val forecastDataFlow = forecastDataMutableFlow.asSharedFlow()

    private val networkErrorsDataMutableFlow = MutableSharedFlow<HandleOnce<Failure>>(1, 0, BufferOverflow.DROP_OLDEST)
    val networkErrorsDataFlow = networkErrorsDataMutableFlow.asSharedFlow()

    fun searchForecast(input: String, cached: Boolean) {
        loadForecastUseCase(LoadForecastUseCase.Params(input, cached)) {
            it.either({ it ->
                networkErrorsDataMutableFlow.tryEmit(HandleOnce<Failure>(it))
            }, { it ->
                forecastDataMutableFlow.tryEmit(it)
            })
        }
        setCity(input)
    }

    fun getCity(): String = prefs.getString("city", DEFAULT_CITY) ?: DEFAULT_CITY

    private fun setCity(city: String) {
        with (prefs.edit()) {
            putString("city", city)
            apply()
        }
    }

    companion object {
        private const val DEFAULT_CITY = "Moscow"
    }
}
