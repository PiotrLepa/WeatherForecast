package com.example.weatherforecast.db.entity.weather


import com.google.gson.annotations.SerializedName

data class Main(
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)