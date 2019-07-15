package io.github.alxiw.openweatherforecast.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Forecast : RealmObject() {
    var city: String = ""
    var head: String = ""
    var description: String = ""
    @PrimaryKey
    var date: String = ""
    var temperature: Double = 0.0
    var imageUrl: String = ""

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}