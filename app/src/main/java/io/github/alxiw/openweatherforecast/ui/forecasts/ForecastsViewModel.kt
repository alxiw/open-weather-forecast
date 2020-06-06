package io.github.alxiw.openweatherforecast.ui.forecasts

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.model.Forecast
import io.github.alxiw.openweatherforecast.model.ForecastResult
import io.realm.RealmResults

class ForecastsViewModel(
    private val repository: WeatherRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    var cached : Boolean = false

    private val queryLiveData = MutableLiveData<String>()
    private val forecastResult: LiveData<ForecastResult> = Transformations.map(queryLiveData) {
        repository.search(it, cached)
    }

    val forecasts: LiveData<RealmResults<Forecast>> = Transformations.switchMap(forecastResult) {
        it.data
    }
    val networkErrors: LiveData<String> = Transformations.switchMap(forecastResult) {
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
