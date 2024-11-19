package io.github.alxiw.openweatherforecast.ui.forecasts

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.data.model.ForecastResult
import io.realm.RealmResults

class ForecastsViewModel(
    private val repository: WeatherRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private var cached : Boolean = false

    private val queryLiveData = MutableLiveData<String>()
    private val forecastResult: LiveData<ForecastResult> = queryLiveData.map {
        repository.search(it, cached)
    }

    val forecasts: LiveData<RealmResults<Forecast>> = forecastResult.switchMap {
        it.data
    }
    val networkErrors: LiveData<String> = forecastResult.switchMap {
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
