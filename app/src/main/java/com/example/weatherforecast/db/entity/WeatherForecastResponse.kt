package com.example.weatherforecast.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_forecast")
data class WeatherForecastResponse(
    @SerializedName("list")
    val weathers: List<WeatherResponse>,
    @Embedded(prefix = "city_")
    val city: City,
    @PrimaryKey
    val id: Int = city.id
)