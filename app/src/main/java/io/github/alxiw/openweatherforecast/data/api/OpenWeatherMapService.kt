package io.github.alxiw.openweatherforecast.data.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    @GET("data/2.5/forecast")
    fun loadWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric"
    ): Single<ForecastResponse>
}
