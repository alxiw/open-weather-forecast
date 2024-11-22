package io.github.alxiw.openweatherforecast.domain.type

sealed class Failure(val message: String) {

    class NetworkConnectionError(message: String) : Failure(message)

    class ServerError(message: String) : Failure(message)

    class DataCached(message: String) : Failure(message)

}
