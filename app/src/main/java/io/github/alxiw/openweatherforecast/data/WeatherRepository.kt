package io.github.alxiw.openweatherforecast.data

import io.github.alxiw.openweatherforecast.data.api.WeatherRemote
import io.github.alxiw.openweatherforecast.data.api.model.CityResponse
import io.github.alxiw.openweatherforecast.data.api.model.ForecastResponse
import io.github.alxiw.openweatherforecast.data.cache.WeatherCache
import io.github.alxiw.openweatherforecast.data.model.City
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.data.model.util.CityConverter
import io.github.alxiw.openweatherforecast.data.model.util.ForecastConverter
import io.github.alxiw.openweatherforecast.domain.type.Either
import io.github.alxiw.openweatherforecast.domain.type.Failure
import io.github.alxiw.openweatherforecast.domain.type.flatMap
import io.github.alxiw.openweatherforecast.log.Hello

class WeatherRepository(
    private val remote: WeatherRemote,
    private val cache: WeatherCache
) {

    var currentCity: City? = null

    fun search(input: String, cached: Boolean): Either<Failure, List<Forecast>> {
        val query = input.trim().lowercase()
        currentCity = null

        return remote.loadCity(query, cached)
            .flatMap(
                { failure -> handleLoadCityFailure(failure, query) },
                { cityResponse -> handleLoadCitySuccess(cityResponse, query) }
            )
            .flatMap { city ->
                remote.loadForecast(city.lat, city.lon, cached)
            }
            .flatMap(
                { failure ->
                    handleLoadForecastFailure(
                        failure,
                        currentCity ?: return@flatMap Either.Left(failure)
                    )
                },
                { forecastResponse ->
                    handleLoadForecastSuccess(
                        forecastResponse,
                        currentCity ?: return@flatMap Either.Right(emptyList())
                    )
                }
            )
    }

    fun getByKey(key: String): Forecast? {
        return ForecastConverter.fromRealm(cache.getForecastItemByKey(key) ?: return null)
    }

    private fun handleLoadCityFailure(failure: Failure, query: String): Either<Failure, City> {
        val cityRealm = cache.getCityByQuery(query)
        return cityRealm?.let {
            val city = CityConverter.fromRealm(it)
            currentCity = city
            Either.Right(city)
        } ?: run {
            Either.Left(failure)
        }
    }

    private fun handleLoadCitySuccess(
        cityResponse: CityResponse,
        query: String
    ): Either<Failure, City> {
        val queryRealm = CityConverter.fromResponseToRealm(query, cityResponse)
        val city: City = queryRealm.city!!.let { CityConverter.fromRealm(it) }
        currentCity = city

        cache.insertQueryWithCity(queryRealm) { result ->
            Hello.d(TAG, "query ${result.first} with city key ${result.second} successfully inserted")
        }

        return Either.Right(city)
    }

    private fun handleLoadForecastFailure(
        failure: Failure,
        city: City
    ): Either<Failure, List<Forecast>> {
        val forecast = ForecastConverter.fromRealm(cache.getForecastByCity(city.name, city.country))
        if (forecast.isEmpty()) {
            return Either.Left(failure)
        }

        return Either.Right(forecast)
    }

    private fun handleLoadForecastSuccess(
        forecastResponse: ForecastResponse,
        city: City
    ): Either<Failure, List<Forecast>> {
        val cityName = city.name
        val countryName = city.country

        var forecastRealm = ForecastConverter.fromResponseToRealm(forecastResponse, cityName, countryName)
        cache.insertForecast(forecastRealm) { result ->
            Hello.d(TAG, "$result forecast items successfully inserted")
        }

        val forecast = ForecastConverter.fromRealm(cache.getForecastByCity(cityName, countryName))

        return Either.Right(forecast)
    }
}

private const val TAG = "WEATHER REPOSITORY"
