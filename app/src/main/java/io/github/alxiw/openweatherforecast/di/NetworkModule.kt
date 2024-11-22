package io.github.alxiw.openweatherforecast.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.alxiw.openweatherforecast.data.api.AuthInterceptor
import io.github.alxiw.openweatherforecast.data.api.ApiService
import io.github.alxiw.openweatherforecast.data.api.NetworkHandler
import io.github.alxiw.openweatherforecast.data.api.Request
import io.github.alxiw.openweatherforecast.data.api.WeatherRemote
import io.github.alxiw.openweatherforecast.log.Hello
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://api.openweathermap.org/"

val networkModule = module {

    factory { NetworkHandler(get()) }
    factory { Request(get()) }
    factory { GsonBuilder().setLenient().create() as Gson }
    factory {
        with(OkHttpClient.Builder()) {
            addInterceptor(AuthInterceptor())
            addInterceptor(HttpLoggingInterceptor(Hello.okHttpLogger).apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            build()
        } as OkHttpClient
    }
    factory {
        with(Retrofit.Builder()) {
            baseUrl(BASE_URL)
            client(get())
            addConverterFactory(GsonConverterFactory.create(get()))
            build()
        }.create(ApiService::class.java) as ApiService
    }
    factory { WeatherRemote(get(), get()) }
}
