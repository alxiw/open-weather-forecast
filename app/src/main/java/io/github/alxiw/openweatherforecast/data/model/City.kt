package io.github.alxiw.openweatherforecast.data.model

data class City(
    val key: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)
