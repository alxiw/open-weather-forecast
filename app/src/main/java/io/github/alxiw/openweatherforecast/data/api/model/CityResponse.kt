package io.github.alxiw.openweatherforecast.data.api.model

import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("name")
    val city: String,
    @SerializedName("local_names")
    val localNames: Map<String, String>?,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("country")
    val country: String,
    @SerializedName("state")
    val state: String,
)
