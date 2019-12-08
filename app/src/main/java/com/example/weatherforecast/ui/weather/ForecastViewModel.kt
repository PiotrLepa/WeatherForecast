package com.example.weatherforecast.ui.weather

import androidx.lifecycle.*
import com.example.weatherforecast.db.entity.WeatherForecastResponse
import com.example.weatherforecast.repository.WeatherForecastRepository
import com.example.weatherforecast.CityProvider
import com.example.weatherforecast.util.wrapper.Resource
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val repo: WeatherForecastRepository,
    private val cityProvider: CityProvider
) : ViewModel() {

    private val cityIdLive = MutableLiveData<Int>()
    val loadedWeatherForecast: LiveData<Resource<WeatherForecastResponse>> =
        Transformations.switchMap(cityIdLive) {
            return@switchMap repo.fetchForecast(it)
        }

    fun onFragmentCreated(selectedCityId: Int?) {
        if (selectedCityId != null) {
            fetchWeatherForecast(selectedCityId)
            cityProvider.saveCityId(selectedCityId)
        } else {
            val cachedCityId = cityProvider.getCityId()
            if (cachedCityId != null) {
                fetchWeatherForecast(cachedCityId)
            } else {
                //TODO first app launch
            }
        }
    }

    fun refreshWeather() {
        cityIdLive.value = cityIdLive.value
    }

    private fun fetchWeatherForecast(cityId: Int) {
        cityIdLive.value = cityId
    }
}
