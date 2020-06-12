package io.github.alxiw.openweatherforecast.data

import android.content.Context
import androidx.work.*
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.realm.Realm
import io.realm.RealmResults
import java.util.concurrent.TimeUnit

class WeatherWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        clearOldForecasts()
        return Result.success()
    }

    private fun clearOldForecasts() {
        val realm = Realm.getDefaultInstance()
        val bound = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
            realm.executeTransaction {
                val old: RealmResults<Forecast> = it
                    .where(Forecast::class.java)
                    .lessThan("date", bound)
                    .findAll()
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
