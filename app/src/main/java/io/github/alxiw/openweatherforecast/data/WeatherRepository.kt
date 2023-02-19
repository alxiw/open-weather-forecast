package io.github.alxiw.openweatherforecast.data

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import io.github.alxiw.openweatherforecast.data.model.ForecastConverter.fromResponse
import io.github.alxiw.openweatherforecast.data.api.ForecastResponse
import io.github.alxiw.openweatherforecast.data.api.OpenWeatherMapService
import io.github.alxiw.openweatherforecast.data.db.WeatherLocalCache
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.data.model.ForecastResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherRepository(
    private val service: OpenWeatherMapService,
    private val cache: WeatherLocalCache
) {

    private val networkErrors = MutableLiveData<String>()
    private var isRequestInProgress = false

    fun getByKey(key: String): Forecast? {
        val data = cache.forecastsByKey(key)
        return data.first()
    }

    fun search(query: String, cached: Boolean): ForecastResult {
        if (!cached) {
            requestAndSaveData(query)
        }
        val data = cache.forecastsByCity(query)
        return ForecastResult(data, networkErrors)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchForecasts(
            service,
            query,
            { forecasts ->
                cache.insert(forecasts) {
                    networkErrors.postValue(null)
                    isRequestInProgress = false
                }
            },
            { error ->
                networkErrors.postValue(error)
                isRequestInProgress = false
            }
        )
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
                    onSuccess(fromResponse(response))
                },
                { e: Throwable ->
                    onError(e.message ?: "Unknown error")
                }
            )
    }
}
