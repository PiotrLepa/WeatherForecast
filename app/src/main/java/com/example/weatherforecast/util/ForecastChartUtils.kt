package com.example.weatherforecast.util

import com.example.weatherforecast.db.entity.WeatherResponse
import com.github.mikephil.charting.data.Entry
import org.joda.time.DateTime

class ForecastChartUtils {

    companion object {

        fun getTempEntries(weathers: List<WeatherResponse>): List<Entry> {
            val temperatures = weathers.map {
                it.main.temp.toFloat()
            }
            return temperatures.mapIndexed { index, temp ->
                Entry(index.toFloat(), temp)
            }
        }

        fun getHours(weathers: List<WeatherResponse>): List<DateTime.Property> {
            val dateFormat = WeatherUnitUtils.dateFormat
            return weathers.map {
                DateTime.parse(it.dt_txt, dateFormat).hourOfDay()
            }
        }
    }

    data class LineDataValue(
        val tempEntries: List<Entry>,
        val hours: List<DateTime.Property>)
}