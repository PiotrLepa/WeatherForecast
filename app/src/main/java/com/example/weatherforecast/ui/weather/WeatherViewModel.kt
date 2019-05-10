package com.example.weatherforecast.ui.weather

import androidx.lifecycle.ViewModel;
import com.example.weatherforecast.repository.WeatherForecastRepository
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val repo: WeatherForecastRepository
): ViewModel() {

    fun fetchWeatherTest() = repo.fetchWeatherTest()
}
