package io.github.alxiw.openweatherforecast.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    companion object {
        const val AUTH_QUERY = "APPID"
        const val API_KEY = "6a51b37adb63d18c92c3fb0d8fd075cb"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val url = originalUrl.newBuilder()
                .addQueryParameter(AUTH_QUERY, API_KEY)
                .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}