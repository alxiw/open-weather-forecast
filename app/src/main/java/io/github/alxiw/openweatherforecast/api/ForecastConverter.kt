package io.github.alxiw.openweatherforecast.api

import io.github.alxiw.openweatherforecast.model.Forecast
import java.util.*

object ForecastConverter {

    fun fromResponse(response: ForecastResponse): List<Forecast> {
        val city = response.city.name

        return response.list.map {
            val forecast = Forecast()
            forecast.key = "${city.toLowerCase(Locale.US)}#${it.date}"
            forecast.city = city.toLowerCase(Locale.US)
            forecast.head = it.weather.first().head
            forecast.description = it.weather.first().description
            forecast.date = it.date.toString()
            forecast.temperature = it.main.temperature
            forecast.imageUrl = convertIconIdToUrl(it.weather.first().image)
            forecast
        }
    }

    private fun convertIconIdToUrl(iconId: String) = "https://openweathermap.org/img/w/$iconId.png"
}
