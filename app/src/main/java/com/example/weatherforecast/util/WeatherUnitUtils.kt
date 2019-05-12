package com.example.weatherforecast.util

import com.example.weatherforecast.WEATHER_API_REFRESH_DELAY
import org.joda.time.DateTime
import timber.log.Timber


class WeatherUnitUtils {

    companion object {
        private val CONDITION_IMAGE_URL = "http://openweathermap.org/img/w/"
        private val IMAGE_EXTENSION = ".png"
        private val DEGREE_CODE = "\u00b0"

        private val TEMP_CELSIUS = "\u2103"
        private val TEMP_FAHRENHEIT = "\u2109"
        private val TEMP_KELVIN = "\u212A"
        private val TEMP_UNIT = TEMP_CELSIUS

        private val WIND_SPEED_METRIC_UNIT = " meter/sec"
        private val WIND_IMPERIAL_UNIT = "miles/hour"

        private val ATMOSPHERIC_PRESSURE_UNIT = "hpa"

        private val PERCENT = "%"

        fun formatTemperature(temp: Double): String {
            return roundValue(temp) + TEMP_UNIT
        }

        fun formatWindSpeed(wind: Double): String {
            return roundValue(wind) + WIND_SPEED_METRIC_UNIT
        }

        fun formatWindDegree(wind: Double): String {
            return roundValue(wind) + DEGREE_CODE
        }

        fun formatAtmosphericPressure(pressure: Int): String {
            return pressure.toString() + ATMOSPHERIC_PRESSURE_UNIT
        }

        fun formatAirHumidity(airHumidity: Int): String {
            return airHumidity.toString() + PERCENT
        }

        fun formatVisibility(visibility: Int?): String {
            return if (visibility == null) {
                "-"
            } else {
                 "${visibility}m"
            }
        }

        fun getWeatherCondictionIconUrl(conditionImage: String): String {
            return CONDITION_IMAGE_URL + conditionImage + IMAGE_EXTENSION
        }

        fun formatUpdateTime(updateTime: Long): String {
            val timeDifference = System.currentTimeMillis() / 1000 - updateTime
            Timber.d("formatUpdateTime: timeDifference: $timeDifference")
//            return if (timeDifference < WEATHER_API_REFRESH_DELAY) {
//                "Just Updated"
//            } else {
//                "Updated ${timeDifference / 60} minutes ago"
//            }
            return "Updated ${timeDifference / 60} minutes ago"
        }

        private fun roundValue(value: Double?): String {
            return if (value != null) {
                Math.round(value).toString()
            } else {
                "-"
            }
        }
    }
}