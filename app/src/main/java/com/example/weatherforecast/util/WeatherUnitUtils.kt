package com.example.weatherforecast.util


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

        fun formatTemperature(temp: String): String {
            return roundValue(temp) + TEMP_UNIT
        }

        fun formatWindSpeed(wind: String): String {
            return roundValue(wind) + WIND_SPEED_METRIC_UNIT
        }

        fun formatWindDegree(wind: String): String {
            return roundValue(wind) + DEGREE_CODE
        }

        fun formatAtmosphericPressure(pressure: String): String {
            return roundValue(pressure) + ATMOSPHERIC_PRESSURE_UNIT
        }

        fun formatAirHumidity(airHumidity: String): String {
            return airHumidity + PERCENT
        }

        fun formatVisibility(visibility: String?): String {
            return if (visibility == null) {
                "-"
            } else {
                 "${visibility}m"
            }
        }

        fun getWeatherCondictionIconUrl(conditionImage: String): String {
            return CONDITION_IMAGE_URL + conditionImage + IMAGE_EXTENSION
        }

        private fun roundValue(value: String?): String {
            return if (value != null) {
                Math.round(java.lang.Float.parseFloat(value)).toString()
            } else {
                "-"
            }
        }
    }
}