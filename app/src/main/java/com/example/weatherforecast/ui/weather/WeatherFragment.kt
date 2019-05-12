package com.example.weatherforecast.ui.weather

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.example.weatherforecast.R
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.ui.MainActivity
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatAirHumidity
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatAtmosphericPressure
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatTemperature
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatUpdateTime
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatVisibility
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatWindDegree
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatWindSpeed
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.weather_fragment.*
import timber.log.Timber
import javax.inject.Inject
import com.example.weatherforecast.util.wrapper.Status.LOADING
import com.example.weatherforecast.util.wrapper.Status.SUCCESS
import com.example.weatherforecast.util.wrapper.Status.ERROR
import kotlinx.android.synthetic.main.snippet_weather.*

class WeatherFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherViewModel::class.java)

        setupPullToRefresh()

        val selectedCityWeather = getSelectedCityWeatherArgs()
        if (selectedCityWeather != null) {
            updateUi(selectedCityWeather)
            viewModel.fetchWeather(selectedCityWeather.id)
        } else {
            viewModel.loadLatestFetchedWeather()
        }

        viewModel.weather.observe(viewLifecycleOwner, Observer { 
            Timber.d("onActivityCreated: $it")
            when (it.status) {
                LOADING -> {

                }
                SUCCESS -> {
                    swipeRefreshLayout.isRefreshing = false
                    updateUi(it.data!!)
                }
                ERROR -> {
                    swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(context, "Updating error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupPullToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshWeather()
        }
    }

    private fun getSelectedCityWeatherArgs(): WeatherResponse? {
        val safeArgs: WeatherFragmentArgs by navArgs()
        return safeArgs.selectedCity
    }

    private fun updateUi(data: WeatherResponse) {
        updateToolbar(data)
        weatherCondition.text = data.weather[0].main
        weatherConditionDescription.text = data.weather[0].description

        temperatureText.text = formatTemperature(data.main.temp)
        minTemperatureText.text = formatTemperature(data.main.tempMin)
        maxTemperatureText.text = formatTemperature(data.main.tempMax)

        windSpeedText.text = formatWindSpeed(data.wind.speed)
        windDegreeText.text = formatWindDegree(data.wind.deg)

        atmosphericPressureText.text = formatAtmosphericPressure(data.main.pressure)
        airHumidityText.text = formatAirHumidity(data.main.humidity)
        visibilityText.text = formatVisibility(data.main.humidity)
        cloudinessText.text = data.clouds.all.toString()
    }

    private fun updateToolbar(data: WeatherResponse) {
        (activity as MainActivity).supportActionBar?.title = data.name
        (activity as MainActivity).supportActionBar?.subtitle = formatUpdateTime(data.dt)
    }
}
