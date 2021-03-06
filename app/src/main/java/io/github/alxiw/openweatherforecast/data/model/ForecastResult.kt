package io.github.alxiw.openweatherforecast.data.model

import androidx.lifecycle.LiveData
import io.realm.RealmResults

data class ForecastResult(
    val data: LiveData<RealmResults<Forecast>>,
    val networkErrors: LiveData<String>
)
