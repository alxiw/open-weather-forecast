package io.github.alxiw.openweatherforecast.data.model.util

import io.github.alxiw.openweatherforecast.data.api.model.ForecastResponse
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.data.cache.model.ForecastRealm
import java.util.concurrent.TimeUnit

object ForecastConverter {

    fun fromResponseToRealm(response: ForecastResponse, cityName: String, countryName: String): List<ForecastRealm> {
        val id = response.city.id
        val city = formatString(response.city.name)
        val country = formatString(response.city.country)

        return response.list.map {
            val forecast = ForecastRealm()
            forecast.key = createPrimaryKey(id, city, country, it.txt)
            forecast.city = cityName
            forecast.country = countryName
            forecast.head = it.weather.first().head
            forecast.description = it.weather.first().description
            forecast.date = TimeUnit.SECONDS.toMillis(it.date)
            forecast.temperature = it.main.temperature
            forecast.imageUrl = convertIconIdToUrl(it.weather.first().image)
            forecast
        }
    }

    fun fromRealm(realm: ForecastRealm): Forecast {
        return Forecast(
            realm.key,
            realm.city,
            realm.country,
            realm.head,
            realm.description,
            realm.date,
            realm.temperature,
            realm.imageUrl
        )
    }

    fun fromRealm(list: List<ForecastRealm>): List<Forecast> {
        return list.map(::fromRealm)
    }

    private fun createPrimaryKey(id: Int, name: String, country: String, txt: String): String {
        return "$id#$country#$name#${formatString(txt)}"
    }

    private fun convertIconIdToUrl(iconId: String) = "https://openweathermap.org/img/wn/$iconId@2x.png"
}
