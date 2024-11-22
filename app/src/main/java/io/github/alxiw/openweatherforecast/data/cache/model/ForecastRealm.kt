package io.github.alxiw.openweatherforecast.data.cache.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ForecastRealm : RealmObject {
    @PrimaryKey
    var key = ""
    var city: String = ""
    var country: String = ""
    var head: String = ""
    var description: String = ""
    var date: Long = 0L
    var temperature: Double = 0.0
    var imageUrl: String = ""
}
