package io.github.alxiw.openweatherforecast.data

import android.content.Context
import android.util.Log
import androidx.work.*
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.ui.forecasts.ForecastItem
import io.realm.Realm
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class WeatherWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.i("HELLO", "Weather Worker: ready to remove old forecasts")
        clearOldForecasts()
        return Result.success()
    }

    private fun clearOldForecasts() {
        val realm = Realm.getDefaultInstance()
        val bound = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        val boundFormat = SimpleDateFormat(ForecastItem.DATE_PATTERN, Locale.US).format(Date(bound))
        Log.i("HELLO", "Weather Worker: boundary date is $boundFormat")
            realm.executeTransaction {
                val old: RealmResults<Forecast> = it
                    .where(Forecast::class.java)
                    .lessThan("date", bound)
                    .findAll()
                Log.i("HELLO", "Weather Worker: ${old.size} old forecasts will be removed")
                old.deleteAllFromRealm()
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
