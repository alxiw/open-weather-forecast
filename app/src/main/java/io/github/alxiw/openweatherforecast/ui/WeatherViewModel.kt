package io.github.alxiw.openweatherforecast.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.model.Forecast
import io.github.alxiw.openweatherforecast.model.ForecastResult
import io.realm.RealmResults
import javax.inject.Inject

class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    var currentlySelectedForecast : Forecast? = null

    private val queryLiveData = MutableLiveData<String>()
    private val forecastResult: LiveData<ForecastResult> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val forecasts: LiveData<RealmResults<Forecast>> = Transformations.switchMap(forecastResult) { it ->
        it.data
    }
    val networkErrors: LiveData<String> = Transformations.switchMap(forecastResult) { it ->
        it.networkErrors
    }

    /**
     * Search a repository based on a query string.
     */
    fun searchForecasts(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value

}
