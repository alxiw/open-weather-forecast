package io.github.alxiw.openweatherforecast.data.cache.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CityRealm : RealmObject {
    @PrimaryKey
    var key: String = ""
    var name: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0
    var country: String = ""
}
