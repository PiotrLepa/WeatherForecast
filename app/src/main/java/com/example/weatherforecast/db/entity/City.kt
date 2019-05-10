package com.example.weatherforecast.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.db.entity.weather.Coord

@Entity
data class City(
    @PrimaryKey
    val id: Int,
    val name: String,
    val country: String,
    @Embedded(prefix = "coord_")
    val coord: Coord
)