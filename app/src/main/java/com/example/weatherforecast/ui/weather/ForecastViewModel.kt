package com.example.weatherforecast.ui.weather

import androidx.lifecycle.*
import com.example.weatherforecast.db.entity.WeatherForecastResponse
import com.example.weatherforecast.repository.WeatherForecastRepository
import com.example.weatherforecast.util.wrapper.Resource
import timber.log.Timber
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val repo: WeatherForecastRepository
) : ViewModel() {

    private val cityIdLive = MutableLiveData<Int>()
    val loadedWeatherForecast: LiveData<Resource<WeatherForecastResponse>> =
        Transformations.switchMap(cityIdLive) {
            return@switchMap repo.fetchForecast(it)
        }

    fun refreshWeather() {
        cityIdLive.value = cityIdLive.value
    }

    fun fetchWeatherForecast(cityId: Int) {
        cityIdLive.value = cityId
    }

    fun loadLatestFetchedForecast() {
        val latestForecast = repo.getLatestInsertedForecast()
        latestForecast.observeForever(object : Observer<WeatherForecastResponse> {
            override fun onChanged(forecast: WeatherForecastResponse?) {
                latestForecast.removeObserver(this)
                Timber.d("onChanged: loadLatestFetchedForecast: ${forecast?.city}")
                forecast?.let { cityIdLive.value = it.city.id }
            }
        })
    }
}
