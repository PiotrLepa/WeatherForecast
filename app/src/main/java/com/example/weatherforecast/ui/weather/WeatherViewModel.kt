package com.example.weatherforecast.ui.weather

import androidx.lifecycle.*
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.repository.WeatherForecastRepository
import com.example.weatherforecast.util.wrapper.Resource
import com.example.weatherforecast.util.wrapper.Status
import timber.log.Timber
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val repo: WeatherForecastRepository
): ViewModel() {

    private val shouldRefreshWeather = MutableLiveData<Boolean>()

    val weather: LiveData<Resource<WeatherResponse>> =
        Transformations.switchMap(shouldRefreshWeather) { shouldRefresh ->
            Timber.d("switchMap shouldRefresh: $shouldRefresh")
            return@switchMap repo.fetchWeather(7532542)
        }

    fun fetchWeather() {
        shouldRefreshWeather.value = true
    }
}
