package io.github.alxiw.openweatherforecast.data.cache.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class QueryRealm : RealmObject {
    @PrimaryKey
    var query: String = ""
    var city: CityRealm? = null
}
