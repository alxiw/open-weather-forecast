package io.github.alxiw.openweatherforecast.domain

import io.github.alxiw.openweatherforecast.data.WeatherRepository
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.domain.type.Either
import io.github.alxiw.openweatherforecast.domain.type.Failure

class LoadForecastUseCase(
    private val weatherRepository: WeatherRepository
) : UseCase<List<Forecast>, LoadForecastUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, List<Forecast>> = weatherRepository.search(params.input, params.cached)

    data class Params(val input: String, val cached: Boolean)
}
