package io.github.alxiw.openweatherforecast.data.model.util

import io.github.alxiw.openweatherforecast.data.api.model.CityResponse
import io.github.alxiw.openweatherforecast.data.model.City
import io.github.alxiw.openweatherforecast.data.cache.model.CityRealm
import io.github.alxiw.openweatherforecast.data.cache.model.QueryRealm

object CityConverter {

    fun fromResponseToRealm(query: String, response: CityResponse): QueryRealm {
        val name = response.city
        val country = response.country

        val cityRealm = CityRealm()

        cityRealm.key = createPrimaryKey(name, country)
        cityRealm.name = name
        cityRealm.lat = response.lat
        cityRealm.lon = response.lon
        cityRealm.country = country

        val queryRealm = QueryRealm()

        queryRealm.query = query
        queryRealm.city = cityRealm

        return queryRealm
    }

    fun fromRealm(realm: CityRealm): City {
        return City(
            realm.key,
            realm.name,
            realm.lat,
            realm.lon,
            realm.country
        )
    }

    fun createPrimaryKey(name: String, country: String): String {
        return "${formatString(name)}#${formatString(country)}"
    }
}
