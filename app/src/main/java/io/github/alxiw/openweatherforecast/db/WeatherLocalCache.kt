package io.github.alxiw.openweatherforecast.db

import androidx.lifecycle.LiveData
import io.github.alxiw.openweatherforecast.model.Forecast
import java.util.concurrent.Executor
import io.realm.*

class WeatherLocalCache(
    private val ioExecutor: Executor
) {

    fun insert(forecasts: List<Forecast>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            val realm: Realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val list = RealmList<Forecast>()
            list.addAll(forecasts)
            realm.insertOrUpdate(list)
            realm.commitTransaction()
            realm.close()
            insertFinished()
        }
    }

    fun forecastsByCity(name: String): LiveData<RealmResults<Forecast>> {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(Forecast::class.java)
                .equalTo("city", name.toLowerCase())
                .findAll()
        return result.asLiveData()
    }

    private fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)
}