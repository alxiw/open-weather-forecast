package io.github.alxiw.openweatherforecast.ui.forecasts

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.data.model.ForecastResult
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ForecastsViewModel(
    private val repository: WeatherRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private var cached : Boolean = false

    private val queryMutableFlow = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    private val forecastResult: Flow<ForecastResult> = queryMutableFlow.map {
        repository.search(it, cached)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val forecasts: Flow<ResultsChange<Forecast>> = forecastResult.flatMapLatest {
        it.data
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val networkErrors: Flow<String?> = forecastResult.flatMapLatest {
        it.networkErrors
    }

    fun searchForecasts(queryString: String, cached: Boolean) {
        this.cached = cached
        queryMutableFlow.tryEmit(queryString)
        setCity(queryString)
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
