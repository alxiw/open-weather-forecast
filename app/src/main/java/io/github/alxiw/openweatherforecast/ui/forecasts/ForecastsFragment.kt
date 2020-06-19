package io.github.alxiw.openweatherforecast.ui.forecasts

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.github.alxiw.openweatherforecast.R
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.ui.details.DetailsFragment
import io.github.alxiw.openweatherforecast.util.hide
import io.github.alxiw.openweatherforecast.util.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ForecastsFragment : Fragment() {

    private val viewModel by viewModel<ForecastsViewModel>()
    private var forceLoadFromCache: Boolean = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyList: TextView
    private lateinit var searchForecast: EditText
    private lateinit var progressBar: ProgressBar
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreSavedInstanceState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forecasts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        val previousHeader = ForecastHeaderFactory.create(context, ForecastHeaderFactory.Type.PREVIOUS)
        val futureHeader = ForecastHeaderFactory.create(context, ForecastHeaderFactory.Type.FUTURE)
        viewModel.forecasts.observe(this.viewLifecycleOwner, Observer {
            if (it.size != 0) {
                showForecasts()
            } else {
                showLoading()
            }
            val previousList = ArrayList<ForecastItem>()
            val futureList = ArrayList<ForecastItem>()
            it.forEach {item ->
                val forecastItem = ForecastItem(item, ::onForecastClicked)
                if (System.currentTimeMillis() >= item.date) {
                    previousList.add(forecastItem)
                } else {
                    futureList.add(forecastItem)
                }
            }
            val sections = ArrayList<Section>()
            if (previousList.isNotEmpty()) {
                val previousSection = Section(previousHeader, previousList)
                sections.add(previousSection)
                val futureSection = Section(futureHeader, futureList)
                sections.add(futureSection)
            } else {
                val futureSection = Section(futureList)
                sections.add(futureSection)
            }
            adapter.update(sections)
        })
        viewModel.networkErrors.observe(this.viewLifecycleOwner, Observer {it ->
            it?.let {
                if (viewModel.forecasts.value?.size == 0) {
                    showEmptyList()
                } else {
                    showForecasts()
                }
                ::onNetworkError.invoke(it)
            }
        })

        val query = viewModel.lastQueryValue() ?: viewModel.getCity()
        viewModel.searchForecasts(query, forceLoadFromCache)
        initSearch(query)
    }

    private fun initViews(view: View) {
        progressBar = view.findViewById(R.id.main_progress_bar)

        searchForecast = view.findViewById(R.id.main_search_edit_text)
        emptyList = view.findViewById(R.id.main_empty_list_text_view)

        recyclerView = view.findViewById(R.id.main_forecasts_recycler_view)
        recyclerView.also {
            val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            it.addItemDecoration(decoration)
            it.layoutManager = LinearLayoutManager(activity)
            it.adapter = adapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        forceLoadFromCache = true
        outState.putBoolean(WEATHER_CACHE_TAG, forceLoadFromCache)
    }

    private fun restoreSavedInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            return
        }
        forceLoadFromCache = savedInstanceState.getBoolean(WEATHER_CACHE_TAG)
    }

    private fun initSearch(query: String) {
        searchForecast.setText(query)
        searchForecast.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        searchForecast.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        searchForecast.text.trim().let {
            if (it.isNotEmpty()) {
                recyclerView.scrollToPosition(0)
                viewModel.searchForecasts(it.toString(), false)
                adapter.clear()
            }
        }
    }

    private fun onForecastClicked(forecast: Forecast) {
        DetailsFragment.newInstance(forecast.key).show(childFragmentManager, DetailsFragment.TAG)
    }

    private fun onNetworkError(line: String) {
        Toast.makeText(activity, "\uD83D\uDE28 $line", Toast.LENGTH_LONG).show()
    }

    private fun showEmptyList() {
        progressBar.hide()
        recyclerView.hide()
        emptyList.show()
    }

    private fun showForecasts() {
        progressBar.hide()
        emptyList.hide()
        recyclerView.show()
    }

    private fun showLoading() {
        emptyList.hide()
        recyclerView.hide()
        progressBar.show()
        Handler().postDelayed({
            if (viewModel.forecasts.value?.size == 0) {
                showEmptyList()
            }
        }, TimeUnit.SECONDS.toMillis(10))
    }

    companion object {
        private const val WEATHER_CACHE_TAG = "forced_load_from_cache"

        fun newInstance(): ForecastsFragment {
            return ForecastsFragment()
        }
    }
}
