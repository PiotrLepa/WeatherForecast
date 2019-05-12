package com.example.weatherforecast.ui.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.example.weatherforecast.db.entity.City
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.repository.WeatherForecastRepository
import com.example.weatherforecast.util.wrapper.Resource
import timber.log.Timber
import javax.inject.Inject

class CitiesViewModel @Inject constructor(
    private val repo: WeatherForecastRepository
): ViewModel() {

    private val fetchCities = MutableLiveData<Boolean>()
    val citiesList: LiveData<List<City>> =
        Transformations.switchMap(fetchCities) {
            return@switchMap repo.getCitiesList()
        }

    private val citiesIds = MutableLiveData<List<Int>>()
    val citiesWeathers: LiveData<Resource<List<WeatherResponse>>> =
        Transformations.switchMap(citiesIds) { citiesIds ->
            Timber.d("switchMap citiesIds: $citiesIds")
            return@switchMap repo.getCitiesWeathersList(citiesIds)
        }

    init {
        fetchCities.value = true
    }

//    fun fetchWeather(cityId: Int) {
//        cityIdLive.value = cityId
//    }

    fun fetchCitiesWeathers(citiesList: List<City>) {
        citiesIds.value = citiesList.map { it.id }
        repo.getCitiesWeathersList(citiesList.map { it.id }).observeForever { 
            Timber.d("fetchCitiesWeathers: $it")
        }
    }

    fun onCityAdded(city: City) {
        repo.addCity(city)
    }
}
