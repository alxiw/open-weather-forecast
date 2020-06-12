package io.github.alxiw.openweatherforecast.data.model

import io.github.alxiw.openweatherforecast.data.api.ForecastResponse
import java.util.*
import java.util.concurrent.TimeUnit

object ForecastConverter {

    fun fromResponse(response: ForecastResponse): List<Forecast> {
        val city = response.city.name

        return response.list.map {
            val forecast = Forecast()
            forecast.key = "${city.toLowerCase(Locale.US)}#${it.date}"
            forecast.city = city.toLowerCase(Locale.US)
            forecast.head = it.weather.first().head
            forecast.description = it.weather.first().description
            forecast.date = TimeUnit.SECONDS.toMillis(it.date)
            forecast.temperature = it.main.temperature
            forecast.imageUrl = convertIconIdToUrl(it.weather.first().image)
            forecast
        }
    }

    private fun convertIconIdToUrl(iconId: String) = "https://openweathermap.org/img/w/$iconId.png"
}
