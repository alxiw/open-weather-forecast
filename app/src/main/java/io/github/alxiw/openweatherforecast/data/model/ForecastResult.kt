package io.github.alxiw.openweatherforecast.data.model

import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

data class ForecastResult(
    val data: Flow<ResultsChange<Forecast>>,
    val networkErrors: Flow<String?>
)
