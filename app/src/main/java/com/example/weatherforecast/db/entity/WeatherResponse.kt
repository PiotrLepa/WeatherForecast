package com.example.weatherforecast.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.db.entity.weather.*

@Entity(tableName = "weather")
data class WeatherResponse(
    @PrimaryKey
    val id: Int,
    val base: String,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    val cod: Int,
    @Embedded(prefix = "coord_")
    val coord: Coord,
    val dt: Long,
    @Embedded(prefix = "main_")
    val main: Main,
    val name: String,
    @Embedded(prefix = "rain")
    val rain: Rain?,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind,
    val insertTime: Long = System.currentTimeMillis()
)