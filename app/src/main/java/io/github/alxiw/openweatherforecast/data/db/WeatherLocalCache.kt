package io.github.alxiw.openweatherforecast.data.db

import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import java.util.Locale

class WeatherLocalCache(private val realm: Realm) {

    suspend fun insert(forecasts: List<Forecast>, insertFinished: () -> Unit) {
        realm.write {
            forecasts.forEach { copyToRealm(it, UpdatePolicy.ALL) }
        }
        insertFinished()
    }

    fun forecastsByCity(name: String): Flow<ResultsChange<Forecast>> {
        val city = name.lowercase(Locale.US)
        return realm.query(Forecast::class, "city = $0", city).sort("date", Sort.ASCENDING).find().asFlow()
    }

    fun forecastsByKey(key: String): RealmResults<Forecast> {
        return realm.query(Forecast::class, "key = $0", key).find()
    }
}
