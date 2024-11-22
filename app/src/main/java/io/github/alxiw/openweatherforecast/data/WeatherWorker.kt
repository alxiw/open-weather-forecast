package io.github.alxiw.openweatherforecast.data

import android.content.Context
import androidx.work.*
import io.github.alxiw.openweatherforecast.data.cache.WeatherCache
import io.github.alxiw.openweatherforecast.log.Hello
import io.github.alxiw.openweatherforecast.ui.model.ForecastItem
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

    private val cache: WeatherCache by inject<WeatherCache>()

    override suspend fun doWork(): Result {
        Hello.d(TAG, "ready to remove outdated forecast items")
        clearOutdatedForecast()
        return Result.success()
    }

    private suspend fun clearOutdatedForecast() {
        val bound = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        val boundFormat = SimpleDateFormat(ForecastItem.DATE_PATTERN, Locale.US).format(Date(bound))
        Hello.d(TAG, "boundary date is $boundFormat")
        cache.removeOutdatedForecast(bound) {
            Hello.d(TAG, "outdated forecast items successfully removed")
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

private const val TAG = "WEATHER WORKER"
