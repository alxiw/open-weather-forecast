package io.github.alxiw.openweatherforecast.data

import android.annotation.SuppressLint
import android.util.Log
import io.github.alxiw.openweatherforecast.data.model.ForecastConverter.fromResponse
import io.github.alxiw.openweatherforecast.data.api.OpenWeatherMapService
import io.github.alxiw.openweatherforecast.data.db.WeatherLocalCache
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.data.model.ForecastResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WeatherRepository(
    private val service: OpenWeatherMapService,
    private val cache: WeatherLocalCache
) {

    private val networkErrors = MutableSharedFlow<String?>(1, 0, BufferOverflow.DROP_OLDEST)
    private var requestJob: Job? = null
    private var insertJob: Job? = null

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
        searchForecasts(
            service,
            query,
            { forecasts ->
                Log.d("HELLO", "SUCCESS: ${forecasts.size}")
                if (forecasts.isNotEmpty()) {
                    Log.d("HELLO", "CITY: ${forecasts[0].city}")
                }
                insertJob?.cancel()
                insertJob = CoroutineScope(Dispatchers.IO).launch {
                    cache.insert(forecasts) {
                        networkErrors.tryEmit(null)
                    }
                }
            },
            { error ->
                Log.d("HELLO", "ERROR: $error")
                networkErrors.tryEmit(error)
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
        if (requestJob?.isActive == true) return
        requestJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.loadWeather(query).execute()
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    onSuccess(fromResponse(responseBody))
                } else {
                    onError(HttpException(response).message ?: "Unknown error")
                }
            } catch (e: Throwable) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
}
