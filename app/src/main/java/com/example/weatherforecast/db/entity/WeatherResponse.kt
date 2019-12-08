package com.example.weatherforecast.db.entity

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.db.entity.weather.*
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "citiesWeathers")
@Parcelize
data class WeatherResponse(
    @PrimaryKey
    val id: Int,
    @Nullable
    val base: String?,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    val cod: Int,
    @Embedded(prefix = "coord_")
    val coord: Coord,
    val dt: Long,
    val dt_txt: String?,
    @Embedded(prefix = "main_")
    val main: Main,
    val name: String,
    @Embedded(prefix = "rain")
    val rain: Rain?,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind
) : Parcelable