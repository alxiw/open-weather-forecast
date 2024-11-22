package io.github.alxiw.openweatherforecast.data.api

import io.github.alxiw.openweatherforecast.data.api.model.CityResponse
import io.github.alxiw.openweatherforecast.data.api.model.ForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("geo/1.0/direct")
    fun loadCity(
        @Query("q") city: String,
        @Query("limit") units: String = "5"
    ): Call<List<CityResponse>>

    @GET("data/2.5/forecast")
    fun loadForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "metric"
    ): Call<ForecastResponse>
}
