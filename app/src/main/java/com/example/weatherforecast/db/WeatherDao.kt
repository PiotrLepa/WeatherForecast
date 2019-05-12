package com.example.weatherforecast.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.db.entity.WeatherResponse

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: WeatherResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeathersList(weathersList: List<WeatherResponse>)

    @Query("SELECT * FROM citiesWeathers WHERE id = :cityId")
    fun getCurrentWeather(cityId: Int): LiveData<WeatherResponse>

    @Query("SELECT * FROM citiesWeathers WHERE id IN (:citiesId)")
    fun getCitiesWeatherList(citiesId: List<Int>): LiveData<List<WeatherResponse>>

    @Query("SELECT * FROM citiesWeathers")
    fun getCitiesId(): LiveData<List<WeatherResponse>>
}