package io.github.alxiw.openweatherforecast.data

import android.content.Context
import androidx.work.*
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.ui.forecasts.ForecastItem
import io.realm.Realm
import io.realm.RealmResults
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class WeatherWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        Timber.i("Weather Worker: ready to remove old forecasts")
        clearOldForecasts()
        return Result.success()
    }

    private fun clearOldForecasts() {
        val realm = Realm.getDefaultInstance()
        val bound = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        val boundFormat = SimpleDateFormat(ForecastItem.DATE_PATTERN, Locale.US).format(Date(bound))
        Timber.i("Weather Worker: boundary date is %s", boundFormat)
            realm.executeTransaction {
                val old: RealmResults<Forecast> = it
                    .where(Forecast::class.java)
                    .lessThan("date", bound)
                    .findAll()
                Timber.i("Weather Worker: %d old forecasts will be removed", old.size)
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
