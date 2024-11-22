package io.github.alxiw.openweatherforecast.data.model

data class Forecast(
    val key: String,
    val city: String,
    val country: String,
    val head: String = "",
    val description: String = "",
    val date: Long = 0L,
    val temperature: Double = 0.0,
    val imageUrl: String = ""
)
