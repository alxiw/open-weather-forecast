package io.github.alxiw.openweatherforecast.api

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("city")
    val city: ForecastResponseCity,
    @SerializedName("list")
    val list: List<ForecastResponseListItem>
) {

    data class ForecastResponseCity (
        @SerializedName("name")
        val name: String
    )

    data class ForecastResponseListItem(
        @SerializedName("dt")
        val date: Long,
        @SerializedName("main")
        val main: ForecastResponseListItemMain,
        @SerializedName("weather")
        val weather: List<ForecastResponseListItemWeather>
    ) {

        data class ForecastResponseListItemMain(
            @SerializedName("temp")
            val temperature: Double
        )

        data class ForecastResponseListItemWeather(
            @SerializedName("main")
            val head: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("icon")
            val image: String
        )
    }
}
