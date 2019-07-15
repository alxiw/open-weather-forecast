package io.github.alxiw.openweatherforecast.data

import androidx.lifecycle.MutableLiveData
import io.github.alxiw.openweatherforecast.api.OpenWeatherMapService
import io.github.alxiw.openweatherforecast.model.Forecast
import io.github.alxiw.openweatherforecast.model.ForecastResult
import io.github.alxiw.openweatherforecast.api.ForecastResponse
import io.github.alxiw.openweatherforecast.db.WeatherLocalCache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository(
    private val service: OpenWeatherMapService,
    private val cache: WeatherLocalCache
) {

    private val networkErrors = MutableLiveData<String>()
    private var isRequestInProgress = false

    fun search(query: String): ForecastResult {
        requestAndSaveData(query)
        val data = cache.forecastsByCity(query)
        return ForecastResult(data, networkErrors)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchForecasts(service, query, { forecasts ->
            cache.insert(forecasts) {
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    private fun searchForecasts(
        service: OpenWeatherMapService,
        query: String,
        onSuccess: (repos: List<Forecast>) -> Unit,
        onError: (error: String) -> Unit
    ) {

        service.loadWeather(query).enqueue(
            object : Callback<ForecastResponse> {
                override fun onFailure(call: Call<ForecastResponse>?, t: Throwable) {
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                    call: Call<ForecastResponse>?,
                    response: Response<ForecastResponse>
                ) {
                    if (response.isSuccessful) {
                        val city = response.body()?.city?.name ?: ""
                        val responseList = response.body()?.list?: emptyList()

                        val list = ArrayList<Forecast>()

                        responseList.forEach {
                            val forecast = Forecast()
                            forecast.city = city.toLowerCase()
                            forecast.head = it.weather.first().head
                            forecast.description = it.weather.first().description
                            forecast.date = it.date.toString()
                            forecast.temperature = it.main.temperature
                            forecast.imageUrl = convertIconIdToUrl(it.weather.first().image)
                            list.add(forecast)
                        }
                        onSuccess(list)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
        )
    }

    private fun convertIconIdToUrl(iconId: String) = "https://openweathermap.org/img/w/$iconId.png"

}