# Open Weather Forecast

An Android application that serves as a wrapper for the [OpenWeather API](https://openweathermap.org/api) and implements the [recommended app architecture](https://developer.android.com/jetpack/guide#recommended-app-arch) using MVVM and Repository patterns, [Realm](https://realm.io/) database, and some of the [Android Architecture Components](https://github.com/android/architecture-components-samples), such as ViewModel, ViewBinding, and WorkManager. The application fetches a 5-day weather forecast for every 3 hours to provide the information for the location that you typed into the search field.

<img src="img/sample.png" alt="drawing" width="200"/>

## Dependencies

* [Kotlin](https://github.com/JetBrains/kotlin) 2.0.21
* [Koin](https://github.com/InsertKoinIO/koin) 3.5.6
* [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) 1.9.0
* [Retrofit](https://github.com/square/retrofit) 2.11.0
* [Coil](https://github.com/coil-kt/coil) 2.7.0
* [Realm](https://github.com/realm/realm-kotlin) 3.0.0
* [Groupie](https://github.com/lisawray/groupie) 2.10.1

## Building

In order to request data, you will need an Open Weather [API key](https://home.openweathermap.org/api_keys). In the project's root directory, open the file `local.properties` and include it following the pattern `apikey="YOUR_API_KEY"`.

## License

[MIT](LICENSE) © [alxiw](https://github.com/alxiw)
