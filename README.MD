# Open Weather Forecast

Simple Android application which represents a wrapper for [OpenWeatherMap API](https://openweathermap.org/api) and developed mostly to practice with MVVM architectural pattern, LiveData and ViewModel from [Android Architecture Components](https://github.com/googlesamples/android-architecture-components) and Realm database. The application shows 5 day weather forecast for every 3 hours for the city entered in the corresponding field. Also, the application handles configuration changes and implements repository pattern storing cache data locally.

<img src="img/sample.png" alt="drawing" width="200"/>

## Dependencies

* [Kotlin](https://github.com/JetBrains/kotlin) 1.3.72
* [Dagger](https://github.com/google/dagger) 2.28
* [Retrofit](https://github.com/square/retrofit) 2.9.0
* [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor) 4.7.2
* [RxJava](https://github.com/ReactiveX/RxJava) 2.2.19
* [RxAndroid](https://github.com/ReactiveX/RxAndroid) 2.1.1
* [RecyclerViewHelper](https://github.com/nisrulz/recyclerviewhelper) 25.4.0
* [Glide](https://github.com/bumptech/glide) 4.11.0
* [Realm](https://github.com/realm/realm-java) 5.12.0

## Building

Run [Gradle](https://github.com/gradle/gradle) from the root directory of the project to build it.

``` bash
./gradlew build
```

After completion there will be two directories in app/build/outputs/apk with apk files for debug and release.

## License

[MIT](LICENSE) © [alxiw](https://github.com/alxiw)
