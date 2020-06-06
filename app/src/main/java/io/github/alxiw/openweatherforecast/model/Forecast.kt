package io.github.alxiw.openweatherforecast.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Forecast : RealmObject() {
    @PrimaryKey
    var key = ""
    var city: String = ""
    var head: String = ""
    var description: String = ""
    var date: String = ""
    var temperature: Double = 0.0
    var imageUrl: String = ""
}
