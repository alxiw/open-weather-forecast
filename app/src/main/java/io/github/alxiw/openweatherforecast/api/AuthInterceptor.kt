package io.github.alxiw.openweatherforecast.api

import io.github.alxiw.openweatherforecast.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    companion object {
        private const val AUTH_QUERY = "APPID"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val url = originalUrl.newBuilder()
            .addQueryParameter(AUTH_QUERY, BuildConfig.API_KEY)
            .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}
