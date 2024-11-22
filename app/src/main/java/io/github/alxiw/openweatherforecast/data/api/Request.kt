package io.github.alxiw.openweatherforecast.data.api

import io.github.alxiw.openweatherforecast.domain.type.Either
import io.github.alxiw.openweatherforecast.domain.type.Failure
import retrofit2.Call

class Request(private val networkHandler: NetworkHandler) {

    fun <T, R> make(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
        return when (networkHandler.isConnected) {
            true -> execute(call, transform)
            false -> Either.Left(Failure.NetworkConnectionError("network unavailable"))
        }
    }

    private fun <T, R> execute(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
        return try {
            val response = call.execute()
            val body = response.body()
            when (response.isSuccessful && body != null) {
                true -> Either.Right(transform(body))
                false -> Either.Left(Failure.ServerError(response.code().toString()))
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.NetworkConnectionError(exception.message ?: "error during network request execution"))
        }
    }
}
