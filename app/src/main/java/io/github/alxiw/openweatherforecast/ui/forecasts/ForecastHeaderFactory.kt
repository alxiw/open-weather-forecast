package io.github.alxiw.openweatherforecast.ui.forecasts

import android.content.Context
import android.widget.TextView
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import io.github.alxiw.openweatherforecast.R

class ForecastHeaderFactory {

    companion object {
        fun create(context: Context?, type: Type) = when (type) {
            Type.PREVIOUS -> Section(object : Item() {
                override fun bind(holder: GroupieViewHolder, position: Int) {
                    val text: TextView = holder.containerView.findViewById(R.id.item_header_text)
                    text.text = context?.getString(R.string.header_previous)
                }

                override fun getLayout(): Int {
                    return R.layout.item_header
                }
            })
            Type.FUTURE -> Section(object : Item() {
                override fun bind(holder: GroupieViewHolder, position: Int) {
                    val text: TextView = holder.containerView.findViewById(R.id.item_header_text)
                    text.text = context?.getString(R.string.header_future)
                }

                override fun getLayout(): Int {
                    return R.layout.item_header
                }
            })
        }
    }

    enum class Type {
        PREVIOUS, FUTURE
    }
}
