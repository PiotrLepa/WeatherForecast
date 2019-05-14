package com.example.weatherforecast.repository

import androidx.lifecycle.LiveData
import com.example.weatherforecast.AppExecutors
import com.example.weatherforecast.FORECAST_API_REFRESH_DELAY_SECONDS
import com.example.weatherforecast.WEATHER_API_REFRESH_DELAY_SECONDS
import com.example.weatherforecast.api.ApiResponse
import com.example.weatherforecast.api.OpenWeatherApiService
import com.example.weatherforecast.db.CityDao
import com.example.weatherforecast.db.WeatherDao
import com.example.weatherforecast.db.entity.City
import com.example.weatherforecast.db.entity.WeatherForecastResponse
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.db.entity.WeathersListResponse
import com.example.weatherforecast.util.RateLimiter
import com.example.weatherforecast.util.WeatherUnitUtils
import com.example.weatherforecast.util.wrapper.Resource
import org.joda.time.DateTime
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherForecastRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val weatherDao: WeatherDao,
    private val cityDao: CityDao,
    private val apiService: OpenWeatherApiService
) {

    private val weatherRateLimiter = RateLimiter<Int>(WEATHER_API_REFRESH_DELAY_SECONDS, TimeUnit.SECONDS)
    private val forecastRateLimiter = RateLimiter<Int>(FORECAST_API_REFRESH_DELAY_SECONDS, TimeUnit.SECONDS)

    fun addCity(city: City) {
        appExecutors.diskIO().execute {
            cityDao.insertCity(city)
        }
    }

    fun getCitiesList(): LiveData<List<City>> {
        return cityDao.getCitiesList()
    }

    fun getLatestInsertedWeather(): LiveData<WeatherResponse> {
        return weatherDao.getLatestInsertedWeather()
    }

    fun fetchForecast(cityId: Int): LiveData<Resource<WeatherForecastResponse>> {
        return object: NetworkBoundResource<WeatherForecastResponse, WeatherForecastResponse>(appExecutors) {
            override fun saveCallResult(item: WeatherForecastResponse) {
                weatherDao.insertWeatherForecast(item)
            }

            override fun shouldFetch(data: WeatherForecastResponse?): Boolean {
                Timber.d("shouldFetch: Forecast: ${data == null || forecastRateLimiter.shouldFetch(data.id)}")
                return data == null || forecastRateLimiter.shouldFetch(data.id)
            }

            override fun loadFromDb(): LiveData<WeatherForecastResponse> {
                return weatherDao.getWeatherForecast(cityId)
            }

            override fun createCall(): LiveData<ApiResponse<WeatherForecastResponse>> {
                return apiService.getForecast(cityId)
            }
        }.asLiveData()
    }

    fun fetchWeather(cityId: Int): LiveData<Resource<WeatherResponse>> {
        return object: NetworkBoundResource<WeatherResponse, WeatherResponse>(appExecutors) {
            override fun saveCallResult(item: WeatherResponse) {
                weatherDao.insertWeather(item)
            }

            override fun shouldFetch(data: WeatherResponse?) = data == null || weatherRateLimiter.shouldFetch(data.id)

            override fun loadFromDb(): LiveData<WeatherResponse> {
                return weatherDao.getCityWeather(cityId)
            }

            override fun createCall(): LiveData<ApiResponse<WeatherResponse>> {
                return apiService.getWeather(cityId)
            }

            override fun onFetchFailed() {
            }
        }.asLiveData()
    }

    fun getCitiesWeathersList(citiesIds: List<Int>): LiveData<Resource<List<WeatherResponse>>> {
        return object: NetworkBoundResource<List<WeatherResponse>, WeathersListResponse>(appExecutors) {
            override fun saveCallResult(item: WeathersListResponse) {
                weatherDao.insertWeathersList(item.weathers)
            }

            override fun shouldFetch(data: List<WeatherResponse>?): Boolean {
                if (data == null || data.size != citiesIds.size) return true
                val shouldFetchList = data.map {
                    return weatherRateLimiter.shouldFetch(it.id)
                }
                return !shouldFetchList.contains(true)
            }

            override fun loadFromDb(): LiveData<List<WeatherResponse>> {
                return weatherDao.getCitiesWeathersList(citiesIds)
            }

            override fun createCall(): LiveData<ApiResponse<WeathersListResponse>> {
                Timber.d("createCall: ${parseString(citiesIds.toString())}")
                return apiService.getCitiesWeathers(parseString(citiesIds.toString()))
            }

        }.asLiveData()
    }

    private fun parseString(text: String) = text.substring(1, text.length - 1).replace(" ", "")
}