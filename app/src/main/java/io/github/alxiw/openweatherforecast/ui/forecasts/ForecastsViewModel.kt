package io.github.alxiw.openweatherforecast.ui.forecasts

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.data.model.ForecastResult
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class ForecastsViewModel(
    private val repository: WeatherRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private var cached : Boolean = false

    private val queryLiveData = MutableLiveData<String>()
    private val forecastResult: Flow<ForecastResult> = queryLiveData.map {
        repository.search(it, cached)
    }.asFlow()

    val forecasts: Flow<ResultsChange<Forecast>> = forecastResult.flatMapLatest {
        it.data
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val networkErrors: Flow<String?> = forecastResult.flatMapLatest {
        it.networkErrors
    }

    fun searchForecasts(queryString: String, cached: Boolean) {
        this.cached = cached
        queryLiveData.postValue(queryString)
        setCity(queryString)
    }

    fun lastQueryValue(): String? = queryLiveData.value

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
