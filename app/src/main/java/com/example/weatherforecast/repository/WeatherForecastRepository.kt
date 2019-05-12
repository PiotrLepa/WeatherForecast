package com.example.weatherforecast.repository

import androidx.lifecycle.LiveData
import com.example.weatherforecast.AppExecutors
import com.example.weatherforecast.api.ApiResponse
import com.example.weatherforecast.api.OpenWeatherApiService
import com.example.weatherforecast.db.CityDao
import com.example.weatherforecast.db.WeatherDao
import com.example.weatherforecast.db.entity.City
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.db.entity.WeathersListResponse
import com.example.weatherforecast.util.wrapper.Resource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherForecastRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val weatherDao: WeatherDao,
    private val cityDao: CityDao,
    private val apiService: OpenWeatherApiService
) {

    fun addCity(city: City) {
        appExecutors.diskIO().execute {
            cityDao.insertCity(city)
        }
    }

    fun getCitiesList(): LiveData<List<City>> {
        return cityDao.getCitiesList()
    }

    fun fetchWeather(cityId: Int): LiveData<Resource<WeatherResponse>> {
        return object: NetworkBoundResource<WeatherResponse, WeatherResponse>(appExecutors) {
            override fun saveCallResult(item: WeatherResponse) {
                weatherDao.insertWeather(item)
            }

            override fun shouldFetch(data: WeatherResponse?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<WeatherResponse> {
                return weatherDao.getCurrentWeather(cityId)
            }

            override fun createCall(): LiveData<ApiResponse<WeatherResponse>> {
                return apiService.getWeather(cityId)
            }

            override fun onFetchFailed() {
            }
        }.asLiveData()
    }

    fun getCitiesWeathersList(citiesIds: List<Int>): LiveData<Resource<List<WeatherResponse>>> {
        return object: NetworkBoundResource< List<WeatherResponse>, WeathersListResponse>(appExecutors) {
            override fun saveCallResult(item: WeathersListResponse) {
                weatherDao.insertWeathersList(item.weathers)
            }

            override fun shouldFetch(data: List<WeatherResponse>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<WeatherResponse>> {
                return weatherDao.getCitiesWeatherList(citiesIds)
            }

            override fun createCall(): LiveData<ApiResponse<WeathersListResponse>> {
                Timber.d("createCall: ${parseString(citiesIds.toString())}")
                return apiService.getCitiesWeathers(parseString(citiesIds.toString()))
            }

        }.asLiveData()
    }

    private fun parseString(text: String) = text.substring(1, text.length - 1).replace(" ", "")
}