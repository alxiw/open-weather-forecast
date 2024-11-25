package io.github.alxiw.openweatherforecast.data.cache

import io.github.alxiw.openweatherforecast.data.cache.model.CityRealm
import io.github.alxiw.openweatherforecast.data.cache.model.ForecastRealm
import io.github.alxiw.openweatherforecast.data.cache.model.QueryRealm
import io.github.alxiw.openweatherforecast.log.Hello
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.query.Sort

class WeatherCache(private val realm: Realm) {

    fun insertQueryWithCity(query: QueryRealm, insertFinished: (Pair<String, String>) -> Unit) {
        val city = query.city ?: return

        realm.writeBlocking {
            copyToRealm(city, UpdatePolicy.ALL)
            copyToRealm(query, UpdatePolicy.ALL)
        }
        insertFinished(Pair<String, String>(query.query, city.key))
    }

    fun getCityByQuery(query: String): CityRealm? {
        val results = realm.query(QueryRealm::class, "query = $0", query).find()
        if (results.isEmpty()) {
            Hello.d(TAG, "no city in cache with query $query")
            return null
        }

        return realm.copyFromRealm(results.first()).city
    }

    fun insertForecast(forecast: List<ForecastRealm>, insertFinished: (Int) -> Unit) {
        realm.writeBlocking {
            forecast.map { copyToRealm(it, UpdatePolicy.ALL) }
        }
        insertFinished(forecast.size)
    }

    fun getForecastByCity(city: String, country: String): List<ForecastRealm> {
        val results = realm.query(ForecastRealm::class, "city = $0 && country = $1", city, country)
            .sort("date", Sort.ASCENDING).find()

        return realm.copyFromRealm(results)
    }

    fun getForecastItemByKey(key: String): ForecastRealm? {
        val results = realm.query(ForecastRealm::class, "key = $0", key).find()
        if (results.isEmpty()) {
            Hello.d(TAG, "no forecast in cache with key $key")
            return null
        }

        return results.first()
    }

    suspend fun removeOutdatedForecast(bound: Long, deleteFinished: () -> Unit) {
        val result = realm.query(ForecastRealm::class, "date < $0", bound).find()
        Hello.d(TAG, "${result.size} outdated forecast items will be removed")

        realm.write {
            result.forEach { forecast ->
                findLatest(forecast)?.let { value -> delete(value) }
            }
        }
        deleteFinished()
    }
}

private const val TAG = "WEATHER CACHE"
