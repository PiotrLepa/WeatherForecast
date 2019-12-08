package com.example.weatherforecast.db

import androidx.room.TypeConverter
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.db.entity.weather.Weather
import com.example.weatherforecast.util.fromJson
import com.google.gson.Gson

object ListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToWeatherList(json: String): List<Weather> {
        return Gson().fromJson<List<Weather>>(json)
    }

    @TypeConverter
    @JvmStatic
    fun weatherListToString(list: List<Weather>): String {
        return Gson().toJson(list)
    }



    @TypeConverter
    @JvmStatic
    fun stringToForecastWeatherList(json: String): List<WeatherResponse> {
        return Gson().fromJson<List<WeatherResponse>>(json)
    }

    @TypeConverter
    @JvmStatic
    fun weatherForecastListToString(list: List<WeatherResponse>): String {
        return Gson().toJson(list)
    }
}