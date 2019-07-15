package io.github.alxiw.openweatherforecast.di

import dagger.Component
import io.github.alxiw.openweatherforecast.ui.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class,
    RepositoryModule::class,
    DatabaseModule::class,
    ServiceModule::class
])
interface AppComponent {

    fun inject(target: MainActivity)

}