package io.github.alxiw.openweatherforecast.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import github.nisrulz.recyclerviewhelper.RVHItemClickListener
import io.github.alxiw.openweatherforecast.OpenWeatherForecastApplication
import io.github.alxiw.openweatherforecast.R
import io.github.alxiw.openweatherforecast.model.Forecast
import io.realm.RealmResults
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: WeatherViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyList: TextView
    private lateinit var searchForecast: EditText
    private lateinit var progressBar: ProgressBar
    private val adapter = WeatherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as OpenWeatherForecastApplication).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(WeatherViewModel::class.java)

        progressBar = findViewById(R.id.main_progress_bar)

        searchForecast = findViewById(R.id.main_search_edit_text)
        emptyList = findViewById(R.id.main_empty_list_text_view)

        recyclerView = findViewById(R.id.main_forecasts_recycler_view)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(RVHItemClickListener(applicationContext,
            RVHItemClickListener.OnItemClickListener { _, position ->
                val item = adapter.getItemAtPosition(position)
                viewModel.currentlySelectedForecast = item
                showDialog(item)
            }
        ))

        viewModel.forecasts.observe(this, Observer<RealmResults<Forecast>> {
            showEmptyList(it?.size == 0)
            val list = ArrayList<Forecast>()
            it.forEach {item ->
                list.add(item)
            }
            adapter.submitList(list)
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 $it", Toast.LENGTH_LONG).show()
        })

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY

        viewModel.searchForecasts(query)

        initSearch(query)

        savedInstanceState?.let {
            viewModel.currentlySelectedForecast?.let {
                showDialog(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
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
                viewModel.searchForecasts(it.toString())
                adapter.submitList(null)
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        progressBar.visibility = View.GONE
        if (show) {
            emptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun showDialog(forecast: Forecast){
        val forecastView = LayoutInflater.from(this).inflate(R.layout.dialog_forecast, null)
        val description = forecastView.findViewById<TextView>(R.id.dialog_description_text_view)
        val image = forecastView.findViewById<ImageView>(R.id.dialog_image_image_view)

        description.text = forecast.description
        forecast.imageUrl?.let {
            Glide.with(this)
                .asDrawable()
                .load(it)
                .into(image!!)
        }

        val dialog =  AlertDialog.Builder(this)
            .setView(forecastView)
            .setTitle(forecast.head)
            .setNeutralButton(android.R.string.ok) { dialog, _ ->
                viewModel.currentlySelectedForecast = null
                dialog.dismiss()
            }.create()

        dialog.show()
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Moscow"
    }
}
