package io.github.alxiw.openweatherforecast.data.api.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("city")
    val city: ForecastResponseCity,
    @SerializedName("list")
    val list: List<ForecastResponseListItem>
) {

    data class ForecastResponseCity (
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("country")
        val country: String
    )

    data class ForecastResponseListItem(
        @SerializedName("dt")
        val date: Long,
        @SerializedName("main")
        val main: ForecastResponseListItemMain,
        @SerializedName("weather")
        val weather: List<ForecastResponseListItemWeather>,
        @SerializedName("dt_txt")
        val txt: String
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
