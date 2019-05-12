package com.example.weatherforecast.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.weatherforecast.AppExecutors
import com.example.weatherforecast.api.ApiResponse
import com.example.weatherforecast.api.OpenWeatherApiService
import com.example.weatherforecast.db.WeatherDao
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.util.wrapper.Resource
import com.example.weatherforecast.util.wrapper.Status
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherForecastRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val weatherDao: WeatherDao,
    private val apiService: OpenWeatherApiService
) {

    fun fetchWeather(cityId: Int): LiveData<Resource<WeatherResponse>> {
        return object: NetworkBoundResource<WeatherResponse, WeatherResponse>(appExecutors) {
            override fun saveCallResult(item: WeatherResponse) {
                weatherDao.insert(item)
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


    class WeatherRefreshError(cause: Throwable) : Throwable(cause.message, cause)
}