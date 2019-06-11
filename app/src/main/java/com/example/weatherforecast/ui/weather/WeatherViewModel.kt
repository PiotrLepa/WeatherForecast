package com.example.weatherforecast.ui.weather

import androidx.lifecycle.*
import com.example.weatherforecast.db.entity.WeatherForecastResponse
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.repository.WeatherForecastRepository
import com.example.weatherforecast.util.ForecastChartUtils
import com.example.weatherforecast.util.ForecastChartUtils.Companion.getHours
import com.example.weatherforecast.util.ForecastChartUtils.Companion.getTempEntries
import com.example.weatherforecast.util.wrapper.Resource
import timber.log.Timber
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val repo: WeatherForecastRepository
): ViewModel() {

    private val cityIdLive = MutableLiveData<Int>()

    val weather: LiveData<Resource<WeatherResponse>> =
        Transformations.switchMap(cityIdLive) { cityId ->
            Timber.d("switchMap WeatherResponse shouldRefresh: $cityId")
            return@switchMap repo.fetchWeather(cityId)
        }

    val weatherForecast: LiveData<Resource<WeatherForecastResponse>> =
        Transformations.switchMap(cityIdLive) { cityId ->
            Timber.d("switchMap WeatherForecastResponse shouldRefresh: $cityId")
            return@switchMap repo.fetchForecast(cityId)
        }

    private val _chartData = MutableLiveData<ForecastChartUtils.LineDataValue>()
    val chartData: LiveData<ForecastChartUtils.LineDataValue>
        get() = _chartData

    fun fetchWeather(cityId: Int) {
        cityIdLive.value = cityId
    }

    fun refreshWeather() {
        cityIdLive.value = cityIdLive.value
    }

    fun loadLatestFetchedWeather() {
        val latestWeather = repo.getLatestInsertedWeather()
        latestWeather.observeForever(object : Observer<WeatherResponse> {
            override fun onChanged(weather: WeatherResponse?) {
                latestWeather.removeObserver(this)
                weather?.let { cityIdLive.value = it.id }
            }
        })
    }

    fun fetchForecast(cityId: Int) {
        repo.fetchForecast(cityId)
    }

    fun onForecastFetched(weathers: List<WeatherResponse>) {
//        val todayWeathers = getTodayWeather(weathers)
        val tempEntries = getTempEntries(weathers)
        val hours = getHours(weathers)

        _chartData.value = ForecastChartUtils.LineDataValue(tempEntries, hours)
    }
}
