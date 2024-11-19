package io.github.alxiw.openweatherforecast.ui.forecasts

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.xwray.groupie.viewbinding.BindableItem
import io.github.alxiw.openweatherforecast.R
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.databinding.ItemForecastBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class ForecastItem(
    private val forecast: Forecast,
    private val context: Context?,
    private val onClick: (Forecast) -> Unit
) : BindableItem<ItemForecastBinding>() {

    override fun initializeViewBinding(view: View): ItemForecastBinding = ItemForecastBinding.bind(view)

    override fun getId(): Long = layout.toLong()

    override fun bind(viewBinding: ItemForecastBinding, position: Int) {

        val temperature: TextView = viewBinding.itemTemperatureTextView
        val image: ImageView = viewBinding.itemImageImageView
        val date: TextView = viewBinding.itemDateTextView

        context?.let {
            temperature.text = it.getString(
                R.string.temperature_template,
                round(forecast.temperature.toFloat()).toInt().toString()
            )
            image.load(forecast.imageUrl) {
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_placeholder)
            }
        }

        date.text = SimpleDateFormat(DATE_PATTERN, Locale.US).format(Date(forecast.date))

        viewBinding.itemForecastContainer.setOnClickListener { onClick.invoke(forecast) }
    }

    override fun getLayout() = R.layout.item_forecast

    companion object {
        const val DATE_PATTERN = "dd MMMM yyyy, HH:mm"
    }
}
