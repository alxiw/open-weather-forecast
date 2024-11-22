package io.github.alxiw.openweatherforecast.data.api

import io.github.alxiw.openweatherforecast.data.api.model.CityResponse
import io.github.alxiw.openweatherforecast.data.api.model.ForecastResponse
import io.github.alxiw.openweatherforecast.domain.type.Either
import io.github.alxiw.openweatherforecast.domain.type.Failure
import io.github.alxiw.openweatherforecast.log.Hello

class WeatherRemote(
    private val request: Request,
    private val service: ApiService
) {

    fun loadCity(query: String, cached: Boolean): Either<Failure, CityResponse> {
        if (cached) {
            return Either.Left(Failure.DataCached("forcefully load city from cache"))
        }

        return request.make(service.loadCity(query)) { result ->
            Hello.d(TAG, "load city success, result list size ${result.size}")
            result.forEachIndexed { index, city ->
                Hello.d(TAG, "#$index loaded city is ${city.city} " +
                        "from ${city.country}")
            }
            result.first()
        }
    }

    fun loadForecast(lat: Double, lon: Double, cached: Boolean): Either<Failure, ForecastResponse> {
        if (cached) {
            return Either.Left(Failure.DataCached("forcefully load forecast from cache"))
        }

        return request.make(service.loadForecast(lat.toString(), lon.toString())) { result ->
            Hello.d(TAG, "load forecast success for location ${result.city.name} " +
                    "from ${result.city.country}, " +
                    "result list size ${result.list.size}")
            result
        }
    }
}

private const val TAG = "WEATHER REMOTE"
