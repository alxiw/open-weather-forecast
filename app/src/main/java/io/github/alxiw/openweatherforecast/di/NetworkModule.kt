package io.github.alxiw.openweatherforecast.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.alxiw.openweatherforecast.data.api.AuthInterceptor
import io.github.alxiw.openweatherforecast.data.api.OpenWeatherMapService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://api.openweathermap.org/"

val networkModule = module {

    factory { GsonBuilder().setLenient().create() as Gson }
    factory {
        with(OkHttpClient.Builder()) {
            addInterceptor(AuthInterceptor())
            addInterceptor(HttpLoggingInterceptor().apply {
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
            addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            build()
        }.create(OpenWeatherMapService::class.java) as OpenWeatherMapService
    }
}
