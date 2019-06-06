package com.example.weatherforecast.util

import com.example.weatherforecast.db.entity.WeatherResponse
import com.github.mikephil.charting.data.Entry
import org.joda.time.DateTime

class ForecastChartUtils {

    companion object {

        fun getTodayWeather(weathers: List<WeatherResponse>): List<WeatherResponse> {
            val dateFormat = WeatherUnitUtils.dateFormat
            val tomorrow = DateTime.now().withTimeAtStartOfDay().plusDays(1).plusSeconds(1) //plus second to get midnight
            return weathers.filter {
                val date = DateTime.parse(it.dt_txt, dateFormat)
                date.isBefore(tomorrow)
            }
        }

        fun getTempEntries(weathers: List<WeatherResponse>): List<Entry> {
            val temperatures = weathers.map {
                it.main.temp.toInt().toFloat()
            }
            return temperatures.mapIndexed { index, temp ->
                Entry(index.toFloat(), temp)
            }
        }

        fun getHoursToEndOfDay(weathers: List<WeatherResponse>): List<DateTime.Property> {
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