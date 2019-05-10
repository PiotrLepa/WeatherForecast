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
    fun insert(weather: WeatherResponse)

    @Query("SELECT * FROM weather WHERE id = :cityId")
    fun getCurrentWeather(cityId: Int): LiveData<WeatherResponse>

    @Query("SELECT * FROM weather ORDER BY insertTime DESC LIMIT 1")
    fun getLatestInsertedWeather(): LiveData<WeatherResponse>

    @Query("SELECT * FROM weather WHERE id IN (:citiesId)")
    fun getCitiesWeatherList(citiesId: List<Int>): LiveData<List<WeatherResponse>>

    @Query("SELECT * FROM weather")
    fun getCitiesId(): LiveData<List<WeatherResponse>>
}