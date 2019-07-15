package io.github.alxiw.openweatherforecast.di

import dagger.Module
import dagger.Provides
import io.github.alxiw.openweatherforecast.api.AuthInterceptor
import io.github.alxiw.openweatherforecast.api.OpenWeatherMapService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ServiceModule {

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/"
    }

    @Provides
    @Named(BASE_URL)
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideInterceptor() : AuthInterceptor = AuthInterceptor()

    @Provides
    @Singleton
    fun provideLogger() : HttpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        .also { it.level = HttpLoggingInterceptor.Level.BASIC }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: AuthInterceptor, logger: HttpLoggingInterceptor) : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(logger)
        .build()

    @Provides
    @Singleton
    fun provideGsonConverterFactory() : GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideGitHubService(@Named(BASE_URL) baseUrl: String, client: OkHttpClient, gson: GsonConverterFactory): OpenWeatherMapService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(gson)
            .build()
            .create(OpenWeatherMapService::class.java)

}