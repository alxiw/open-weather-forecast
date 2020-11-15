# Open Weather Forecast

[![BCH compliance](https://bettercodehub.com/edge/badge/alxiw/open-weather-forecast?branch=master)](https://bettercodehub.com/)

An Android application which represents a wrapper for [OpenWeather API](https://openweathermap.org/api) and implemented using the [MVVM](https://developer.android.com/jetpack/guide#recommended-app-arch) and [Repository](https://developer.android.com/jetpack/guide#recommended-app-arch) patterns, [Realm](https://realm.io/) database, and some of [Android Architecture Components](https://github.com/googlesamples/android-architecture-components), such as [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) and [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel). The application fetches 5-day weather forecast for every 3 hours to provide it for the location, that you typed at the search field.

<img src="img/sample.png" alt="drawing" width="200"/>

## Dependencies

* [Kotlin](https://github.com/JetBrains/kotlin) 1.3.72
* [Koin](https://github.com/InsertKoinIO/koin) 2.0.1
* [Retrofit](https://github.com/square/retrofit) 2.9.0
* [OkHttp](https://github.com/square/okhttp) 4.7.2
* [RxJava](https://github.com/ReactiveX/RxJava) 2.2.19
* [Groupie](https://github.com/lisawray/groupie) 2.8.0
* [Glide](https://github.com/bumptech/glide) 4.11.0
* [Realm](https://github.com/realm/realm-java) 7.0.0

## Building

In order to request data you will need an Open Weather [API key](https://home.openweathermap.org/api_keys). In the project's root directory open the file `local.properties` and include your it following the pattern `apikey="YOUR_API_KEY"`.

## License

[MIT](LICENSE) © [alxiw](https://github.com/alxiw)
