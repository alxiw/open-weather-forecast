package io.github.alxiw.openweatherforecast.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.github.alxiw.openweatherforecast.api.ForecastResponse
import io.github.alxiw.openweatherforecast.api.OpenWeatherMapService
import io.github.alxiw.openweatherforecast.db.WeatherLocalCache
import io.github.alxiw.openweatherforecast.model.Forecast
import io.github.alxiw.openweatherforecast.model.ForecastResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*
import kotlin.collections.ArrayList

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

    @SuppressLint("CheckResult")
    private fun searchForecasts(
        service: OpenWeatherMapService,
        query: String,
        onSuccess: (repos: List<Forecast>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        service.loadWeather(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: ForecastResponse ->
                    val city = response.city.name
                    val responseList = response.list

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
                },
                { e: Throwable ->
                    onError(e.message ?: "Unknown error")
                }
            )
    }

    private fun convertIconIdToUrl(iconId: String) = "https://openweathermap.org/img/w/$iconId.png"

}
