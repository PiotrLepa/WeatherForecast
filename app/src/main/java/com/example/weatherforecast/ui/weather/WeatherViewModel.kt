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

    private val cityIdLive = MutableLiveData<Int>()

    val weather: LiveData<Resource<WeatherResponse>> =
        Transformations.switchMap(cityIdLive) { cityId ->
            Timber.d("switchMap shouldRefresh: $cityId")
            return@switchMap repo.fetchWeather(cityId)
        }

    fun fetchWeather(cityId: Int) {
        cityIdLive.value = cityId
    }

    fun refreshWeather() {
        Timber.d("refreshWeather: cityIdLive.value: ${cityIdLive.value}")
        cityIdLive.value = cityIdLive.value
    }
}
