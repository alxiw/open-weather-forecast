package io.github.alxiw.openweatherforecast.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    @GET("data/2.5/forecast")
    fun loadWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric"
    ): Call<ForecastResponse>
}
