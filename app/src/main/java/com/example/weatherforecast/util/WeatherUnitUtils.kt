package com.example.weatherforecast.util

import android.graphics.drawable.Drawable
import com.example.weatherforecast.R
import com.example.weatherforecast.db.entity.WeatherResponse
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import timber.log.Timber


class WeatherUnitUtils {

    companion object {

        val dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

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

        fun formatAtmosphericPressure(pressure: Double): String {
            return roundValue(pressure) + ATMOSPHERIC_PRESSURE_UNIT
        }

        fun formatAirHumidity(airHumidity: Double): String {
            return roundValue(airHumidity) + PERCENT
        }

        fun formatVisibility(visibility: Double?): String {
            return if (visibility == null) {
                "-"
            } else {
                 "${roundValue(visibility)}m"
            }
        }

        fun formatCloudiness(cloudiness: Double?): String {
            return roundValue(cloudiness) + PERCENT
        }

        fun getWeatherConditionIconUrl(weather: WeatherResponse): Int {
            val sunrise = DateTime(weather.sys.sunrise * 1000)
            val isDay = sunrise.isBeforeNow

            return when(weather.weather[0].id) {
                200, 230 -> R.drawable.ic_light_thunderstorm_rain
                201, 202, 221, 231, 232 -> R.drawable.ic_thunderstorm_rain
                210, 211, 212 -> if(isDay) R.drawable.ic_thunderstorm_day else R.drawable.ic_thunderstorm_night

                in 300..399 -> if(isDay) R.drawable.ic_drizzle_day else R.drawable.ic_drizzle_night

                500, 501 -> R.drawable.ic_light_rain
                502, 599 -> R.drawable.ic_heavy_rain

                600, 601 -> if(isDay) R.drawable.ic_snow_day else R.drawable.ic_snow_night
                in 602..699 -> R.drawable.ic_heavy_snow

                in 701..780 -> R.drawable.ic_fog
                781 -> R.drawable.ic_tornado

                800 -> if(isDay) R.drawable.ic_clear_sky_day else R.drawable.ic_clear_sky_night

                801, 802 -> if(isDay) R.drawable.ic_few_clouds_day else R.drawable.ic_few_clouds_night
                803 -> R.drawable.ic_broken_clouds
                804 -> R.drawable.ic_overcast_clouds

                else -> R.drawable.ic_broken_clouds
            }
        }

        fun formatDate(date: String?): String {
            return if (date == null) {
                ""
            } else {
                val today = LocalDate.now()
                Timber.d("formatDate: today: $today  dateTime: ${DateTime.parse(date, dateFormat).toLocalDate()}")
                when (val dateTime = DateTime.parse(date, dateFormat).toLocalDate().toString()) {
                    today.toString() -> "Today"
                    today.plusDays(1).toString() -> "Tomorrow"
                    else -> dateTime
                }
            }
        }

        fun formatUpdateTime(updateTime: Long): String {
            Timber.d("formatUpdateTime: current: ${System.currentTimeMillis()}   updateTime: $updateTime")
            val timeDifference = System.currentTimeMillis() - updateTime
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