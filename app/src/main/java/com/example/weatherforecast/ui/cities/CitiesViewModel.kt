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

    private val citiesIds = MutableLiveData<List<Int>>()
    val citiesWeathers: LiveData<Resource<List<WeatherResponse>>> =
        Transformations.switchMap(citiesIds) { citiesIds ->
            Timber.d("switchMap citiesWeathers citiesIds: $citiesIds")
            return@switchMap repo.getCitiesWeathersList(citiesIds)
        }

    val citiesList: LiveData<List<City>> by lazy {
        repo.getCitiesList()
    }

    fun fetchCitiesWeathers(citiesList: List<City>) {
        citiesIds.value = citiesList.map { it.id }
    }

    fun onCityAdded(city: City) {
        repo.addCity(city)
    }
}
