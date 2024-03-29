package com.example.weatherforecast.api

import androidx.lifecycle.LiveData
import com.example.weatherforecast.db.entity.WeatherForecastResponse
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.db.entity.WeathersListResponse
import com.example.weatherforecast.util.retrofitAdapters.LiveDataCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber


const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
const val API_KEY = "749561a315b14523a8f5f1ef95e45864"

interface OpenWeatherApiService {

    @GET("weather")
    fun getWeather(@Query("id") cityId: Int): LiveData<ApiResponse<WeatherResponse>>

    @GET("forecast")
    fun getForecast(@Query("id") cityId: Int): LiveData<ApiResponse<WeatherForecastResponse>>

    @GET("group")
    fun getCitiesWeathers(@Query("id") citiesIds: String): LiveData<ApiResponse<WeathersListResponse>>

    companion object {
        operator fun invoke(): OpenWeatherApiService {
            val requestInterceptor = Interceptor {chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("APPID", API_KEY)
                    .addQueryParameter("units", "metric")
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                
                Timber.d("invoke: ${url}")

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(OpenWeatherApiService::class.java)
        }
    }
}