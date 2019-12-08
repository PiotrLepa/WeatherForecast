package com.example.weatherforecast.util

import com.example.weatherforecast.db.entity.WeatherForecastResponse
import com.example.weatherforecast.db.entity.WeatherResponse
import org.joda.time.DateTime
import timber.log.Timber

class WeatherForecastUtils {

    companion object {
        fun getWeathersDaysList(weatherForecast: WeatherForecastResponse): List<List<WeatherResponse>> {
            val weathersList = ArrayList<List<WeatherResponse>>()
            var startDate = DateTime.parse(weatherForecast.weathers[0].dt_txt, WeatherUnitUtils.dateFormat)
            val endDate = DateTime.parse(
                weatherForecast.weathers[weatherForecast.weathers.size-1].dt_txt,
                WeatherUnitUtils.dateFormat)
            do {
                weathersList.add(getWeathersForDay(startDate, weatherForecast))
                startDate = startDate.plusDays(1)
            } while (startDate.isBefore(endDate))

            return weathersList
        }

        private fun getWeathersForDay(
            day: DateTime,
            weatherForecast: WeatherForecastResponse
        ): List<WeatherResponse> {
            // minus / plus seconds to get midnights
            val startDate = day.withTimeAtStartOfDay().minusSeconds(1)
            val endDate = day.withTimeAtStartOfDay().plusDays(1).plusSeconds(2)
            return weatherForecast.weathers.filter {
                val weatherDate = DateTime.parse(it.dt_txt, WeatherUnitUtils.dateFormat)
                return@filter weatherDate.isBefore(endDate) && weatherDate.isAfter(startDate)
            }
        }
    }
}