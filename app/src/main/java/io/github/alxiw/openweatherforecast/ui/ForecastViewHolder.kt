package io.github.alxiw.openweatherforecast.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.alxiw.openweatherforecast.R
import io.github.alxiw.openweatherforecast.model.Forecast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val temperature: TextView = view.findViewById(R.id.item_temperature_text_view)
    private val image: ImageView = view.findViewById(R.id.item_image_image_view)
    private val date: TextView = view.findViewById(R.id.item_date_text_view)

    private var forecast: Forecast? = null

    fun bind(forecast: Forecast?) {
        if (forecast != null) {
            this.forecast = forecast

            temperature.text = itemView.context.getString(R.string.temperature_template,round(forecast.temperature.toFloat()).toInt().toString())
            date.text = SimpleDateFormat("dd MMMM YYYY, HH:mm", Locale.getDefault()).format(Date(forecast.date.toLong() * 1000))

            Glide.with(itemView.context)
                .load(forecast.imageUrl)
                .placeholder(R.drawable.ic_placeholder_black_24dp)
                .into(image)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ForecastViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_forecast, parent, false)
            return ForecastViewHolder(view)
        }
    }
}