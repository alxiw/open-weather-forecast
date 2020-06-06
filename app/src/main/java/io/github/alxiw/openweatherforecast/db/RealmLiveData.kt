package io.github.alxiw.openweatherforecast.db

import io.realm.RealmResults
import io.realm.RealmChangeListener
import androidx.lifecycle.LiveData
import io.realm.RealmModel

class RealmLiveData<T : RealmModel>(private var results: RealmResults<T>) :
    LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> value = results }

    override fun onActive() {
        results.addChangeListener(listener)
        listener.onChange(results)
    }

    override fun onInactive() {
        results.removeChangeListener(listener)
    }
}
