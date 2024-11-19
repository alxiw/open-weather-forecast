package io.github.alxiw.openweatherforecast.data

import android.content.Context
import android.util.Log
import androidx.work.*
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.ui.forecasts.ForecastItem
import io.realm.kotlin.Realm
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.getValue

class WeatherWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val realm: Realm by inject<Realm>()

    override suspend fun doWork(): Result {
        Log.i("HELLO", "Weather Worker: ready to remove old forecasts")
        clearOldForecasts()
        return Result.success()
    }

    private suspend fun clearOldForecasts() {
        val bound = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        val boundFormat = SimpleDateFormat(ForecastItem.DATE_PATTERN, Locale.US).format(Date(bound))
        Log.i("HELLO", "Weather Worker: boundary date is $boundFormat")
        realm.write {
            val old = realm.query<Forecast>(Forecast::class, "date < $0", bound).find()
            Log.i("HELLO", "Weather Worker: ${old.size} old forecasts will be removed")
            delete(old)
        }
    }

    companion object {
        private const val TAG_WORKER = "WEATHER_WORKER"

        @JvmStatic
        fun init(context: Context, interval: Long) {
            val request = PeriodicWorkRequest.Builder(
                    WeatherWorker::class.java,
                    interval,
                    TimeUnit.HOURS
                ).build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    TAG_WORKER,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    request
                )
        }
    }
}
