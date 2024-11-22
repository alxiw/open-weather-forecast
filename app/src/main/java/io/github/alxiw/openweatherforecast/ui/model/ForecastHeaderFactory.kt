package io.github.alxiw.openweatherforecast.ui.model

import android.content.Context
import android.view.View
import android.widget.TextView
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import io.github.alxiw.openweatherforecast.R
import io.github.alxiw.openweatherforecast.databinding.ItemHeaderBinding

class ForecastHeaderFactory {

    companion object {
        fun create(context: Context?, type: Type) = when (type) {
            Type.PREVIOUS -> Section(object : BindableItem<ItemHeaderBinding>() {
                override fun bind(viewBinding: ItemHeaderBinding, position: Int) {
                    val text: TextView = viewBinding.itemHeaderText
                    text.text = context?.getString(R.string.header_previous)
                }

                override fun getLayout(): Int {
                    return R.layout.item_header
                }

                override fun initializeViewBinding(view: View): ItemHeaderBinding {
                    return ItemHeaderBinding.bind(view)
                }

            })
            Type.FUTURE -> Section(object : BindableItem<ItemHeaderBinding>() {
                override fun bind(viewBinding: ItemHeaderBinding, position: Int) {
                    val text: TextView = viewBinding.itemHeaderText
                    text.text = context?.getString(R.string.header_future)
                }

                override fun getLayout(): Int {
                    return R.layout.item_header
                }

                override fun initializeViewBinding(view: View): ItemHeaderBinding {
                    return ItemHeaderBinding.bind(view)
                }
            })
        }
    }

    enum class Type {
        PREVIOUS, FUTURE
    }
}
