package io.github.alxiw.openweatherforecast.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.alxiw.openweatherforecast.model.Forecast

class WeatherAdapter : ListAdapter<Forecast, RecyclerView.ViewHolder>(FORECAST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ForecastViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as ForecastViewHolder).bind(repoItem)
        }
    }

    fun getItemAtPosition(position : Int) : Forecast {
        return getItem(position)!!
    }

    companion object {
        private val FORECAST_COMPARATOR = object : DiffUtil.ItemCallback<Forecast>() {
            override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean =
                true

            override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean =
                oldItem == newItem
        }
    }

}