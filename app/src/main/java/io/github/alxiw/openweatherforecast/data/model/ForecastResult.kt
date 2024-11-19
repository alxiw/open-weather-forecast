package io.github.alxiw.openweatherforecast.data.model

import androidx.lifecycle.LiveData
import io.realm.RealmResults
import kotlinx.coroutines.flow.Flow

data class ForecastResult(
    val data: LiveData<RealmResults<Forecast>>,
    val networkErrors: Flow<String?>
)
