package com.example.weatherforecast.repository

import com.example.weatherforecast.api.OpenWeatherApiService
import com.example.weatherforecast.db.WeatherDao
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherForecastRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val apiService: OpenWeatherApiService
) {

    fun fetchWeatherTest(): Boolean {
        Timber.d("fetchWeatherTest: weatherDao: $weatherDao")
        Timber.d("fetchWeatherTest: apiService: $apiService")
        return true
    }
}