package io.github.alxiw.openweatherforecast.ui.forecasts

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import io.github.alxiw.openweatherforecast.R
import io.github.alxiw.openweatherforecast.model.Forecast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class ForecastItem(
    private val forecast: Forecast,
    private val onClick: (Forecast) -> Unit
) : Item() {

    override fun getId(): Long = layout.toLong()

    override fun bind(holder: GroupieViewHolder, position: Int) {

        val temperature: TextView = holder.containerView.findViewById(R.id.item_temperature_text_view)
        val image: ImageView = holder.containerView.findViewById(R.id.item_image_image_view)
        val date: TextView = holder.containerView.findViewById(R.id.item_date_text_view)

        with(holder) {
            temperature.text = itemView.context.getString(
                R.string.temperature_template,
                round(forecast.temperature.toFloat()).toInt().toString()
            )
            date.text = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(Date(forecast.date.toLong() * 1000))

            Glide.with(itemView.context)
                .load(forecast.imageUrl)
                .placeholder(R.drawable.ic_placeholder_black_24dp)
                .into(image)
        }

        holder.containerView
            .findViewById<View>(R.id.item_forecast_container)
            .setOnClickListener { onClick.invoke(forecast) }
    }

    override fun getLayout() = R.layout.item_forecast

    companion object {
        private const val DATE_PATTERN = "dd MMMM YYYY, HH:mm"
    }
}
