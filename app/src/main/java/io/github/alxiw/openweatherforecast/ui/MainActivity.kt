package io.github.alxiw.openweatherforecast.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.alxiw.openweatherforecast.R
import io.github.alxiw.openweatherforecast.ui.forecasts.ForecastsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_container, ForecastsFragment.newInstance())
                .commitAllowingStateLoss()
        }
    }
}
