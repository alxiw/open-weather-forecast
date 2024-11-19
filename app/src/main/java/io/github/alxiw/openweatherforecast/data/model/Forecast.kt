package io.github.alxiw.openweatherforecast.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Forecast : RealmObject {
    @PrimaryKey
    var key = ""
    var city: String = ""
    var head: String = ""
    var description: String = ""
    var date: Long = 0L
    var temperature: Double = 0.0
    var imageUrl: String = ""
}
