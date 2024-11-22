package io.github.alxiw.openweatherforecast.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import io.github.alxiw.openweatherforecast.R
import io.github.alxiw.openweatherforecast.data.model.Forecast
import io.github.alxiw.openweatherforecast.databinding.FragmentForecastBinding
import io.github.alxiw.openweatherforecast.presentation.ForecastViewModel
import io.github.alxiw.openweatherforecast.ui.model.ForecastHeaderFactory
import io.github.alxiw.openweatherforecast.ui.model.ForecastItem
import io.github.alxiw.openweatherforecast.ui.util.hide
import io.github.alxiw.openweatherforecast.ui.util.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ForecastFragment : Fragment() {

    private val viewModel by viewModel<ForecastViewModel>()

    private val adapter = GroupAdapter<GroupieViewHolder>()

    private var forceLoadFromCache: Boolean = false

    private lateinit var binding: FragmentForecastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreSavedInstanceState(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForecastBinding.bind(view)

        initViews()

        val input = viewModel.getCity()
        viewModel.searchForecast(input, forceLoadFromCache)
        initSearch(input)
    }

    private fun initViews() {
        with(binding) {
            mainForecastRecyclerView.also {
                val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
                it.addItemDecoration(decoration)
                it.layoutManager = LinearLayoutManager(activity)
                it.adapter = adapter
            }
        }

        val previousHeader = ForecastHeaderFactory.Companion.create(
            context,
            ForecastHeaderFactory.Type.PREVIOUS
        )
        val futureHeader = ForecastHeaderFactory.Companion.create(
            context,
            ForecastHeaderFactory.Type.FUTURE
        )

        lifecycleScope.launch {
            viewModel.forecastDataFlow.collect { it: List<Forecast> ->
                if (it.isNotEmpty()) {
                    setShowingState()
                } else {
                    setLoadingState()
                }
                val previousList = ArrayList<ForecastItem>()
                val futureList = ArrayList<ForecastItem>()
                it.forEach { item ->
                    val forecastItem = ForecastItem(item, context, ::onForecastClicked)
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

            }
        }

        lifecycleScope.launch {
            viewModel.networkErrorsDataFlow.collect { it ->
                it.also {
                    // remove live data
                    val list = viewModel.forecastDataFlow.asLiveData().value
                    if (list.isNullOrEmpty()) {
                        setEmptyState()
                    } else {
                        setShowingState()
                    }
                    it.getContentIfNotHandled()?.also { failure ->
                        onNetworkError(failure.message)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(WEATHER_CACHE_TAG, true)
    }

    private fun restoreSavedInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            return
        }
        forceLoadFromCache = savedInstanceState.getBoolean(WEATHER_CACHE_TAG)
    }

    private fun initSearch(input: String) {
        with(binding) {
            mainSearchEditText.apply {
                setText(input)
                setSelection(this.text.length)
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        updateRepoListFromInput()
                        true
                    } else {
                        false
                    }
                }
                setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        updateRepoListFromInput()
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }

    private fun updateRepoListFromInput() {
        with(binding) {
            mainSearchEditText.text.trim().let {
                if (it.isNotEmpty()) {
                    mainForecastRecyclerView.scrollToPosition(0)
                    viewModel.searchForecast(it.toString(), false)
                    adapter.clear()
                }
            }
        }
    }

    private fun onForecastClicked(forecast: Forecast) {
        DetailsFragment.newInstance(forecast.key).show(childFragmentManager, DetailsFragment.TAG)
    }

    private fun onNetworkError(line: String) {
        Toast.makeText(activity, "\uD83D\uDE28 $line", Toast.LENGTH_LONG).show()
    }

    private fun setEmptyState() {
        with(binding) {
            mainProgressBar.hide()
            mainForecastRecyclerView.hide()
            mainEmptyListTextView.show()
        }
    }

    private fun setShowingState() {
        with(binding) {
            mainProgressBar.hide()
            mainEmptyListTextView.hide()
            mainForecastRecyclerView.show()
        }
    }

    private fun setLoadingState() {
        with(binding) {
            mainEmptyListTextView.hide()
            mainForecastRecyclerView.hide()
            mainProgressBar.show()
            lifecycleScope.launch {
                delay(TimeUnit.SECONDS.toMillis(10))
                viewModel.forecastDataFlow.collect { forecast ->
                    if (forecast.isEmpty()) {
                        setEmptyState()
                    }
                }
            }
        }
    }

    companion object {
        private const val WEATHER_CACHE_TAG = "forced_load_from_cache"

        fun newInstance(): ForecastFragment {
            return ForecastFragment()
        }
    }
}
