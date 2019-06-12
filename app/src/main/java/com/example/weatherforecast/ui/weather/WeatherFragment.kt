package com.example.weatherforecast.ui.weather

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.R

import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatAirHumidity
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatAtmosphericPressure
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatTemperature
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatVisibility
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatWindDegree
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatWindSpeed
import com.example.weatherforecast.util.ForecastChartUtils.LineDataValue
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.snippet_weather.*
import android.graphics.Typeface
import com.example.weatherforecast.FORECAST_FRAGMENT_WEATHER_ARG
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatCloudiness
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter


class WeatherFragment : DaggerFragment() {

    companion object {
        fun newInstance(weatherForecast: List<WeatherResponse>): WeatherFragment {
            val bundle = Bundle().apply {
                putParcelableArrayList(FORECAST_FRAGMENT_WEATHER_ARG, ArrayList(weatherForecast))
            }
            return WeatherFragment().apply {
                arguments = bundle
            }
        }
    }

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

        setupChart()

        val dayWeatherForecast = getWeatherArgs()
        if (dayWeatherForecast != null) {
            val currentWeather = dayWeatherForecast[0]
            updateUi(currentWeather)
            viewModel.onForecastFetched(dayWeatherForecast)
        }

        observeChartData()
    }

    private fun getWeatherArgs(): ArrayList<WeatherResponse>? {
        return arguments?.getParcelableArrayList(FORECAST_FRAGMENT_WEATHER_ARG)
    }

    private fun observeChartData() {
        viewModel.chartData.observe(viewLifecycleOwner, Observer {
            Timber.d("onActivityCreated chartData: $it")
            setChartData(it)
        })
    }

    private fun setupChart() {
        chart.setTouchEnabled(false)
        chart.isDragEnabled = false
        val x = chart.xAxis
        x.textColor = Color.WHITE
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(false)
        x.setDrawAxisLine(false)

        chart.axisRight.isEnabled = false
        chart.axisLeft.isEnabled = false

        chart.legend.isEnabled = false
        chart.description.isEnabled = false

        chart.animateXY(2000, 2000)

        chart.invalidate()
    }

    private fun setChartData(lineDataValues: LineDataValue) {
        val tempSet = LineDataSet(lineDataValues.tempEntries, "Temperatures")
        setupLineDataSet(tempSet)

        val data = LineData(tempSet)
        data.setValueTypeface(Typeface.SANS_SERIF)
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(9f)
        data.setDrawValues(true)
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return formatTemperature(value.toDouble())
            }
        })
        chart.data = data
        chart.xAxis.setLabelCount(lineDataValues.hours.size, true)
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return lineDataValues.hours[value.toInt()].asShortText
            }
        }
    }

    private fun setupLineDataSet(set: LineDataSet) {
        set.mode = LineDataSet.Mode.LINEAR
//        set.cubicIntensity = 0.3f
        set.setDrawFilled(true)
        set.setDrawCircles(true)
        set.lineWidth = 2f
        set.circleRadius = 4f
        set.setCircleColor(Color.WHITE)
        set.color = Color.WHITE
        set.fillColor = Color.WHITE
        set.fillAlpha = 100
    }

    private fun updateUi(data: WeatherResponse) {
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
        cloudinessText.text = formatCloudiness(data.clouds.all)
    }
}
