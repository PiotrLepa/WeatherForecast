package com.example.weatherforecast.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.util.ForecastChartUtils.LineDataValue
import com.example.weatherforecast.util.ForecastChartUtils.Companion.getHours
import com.example.weatherforecast.util.ForecastChartUtils.Companion.getTempEntries

class WeatherViewModel: ViewModel() {

    private val _chartData = MutableLiveData<LineDataValue>()
    val chartData: LiveData<LineDataValue>
        get() = _chartData

    fun onForecastFetched(weathers: List<WeatherResponse>) {
        val tempEntries = getTempEntries(weathers)
        val hours = getHours(weathers)

        _chartData.value = LineDataValue(tempEntries, hours)
    }
}
