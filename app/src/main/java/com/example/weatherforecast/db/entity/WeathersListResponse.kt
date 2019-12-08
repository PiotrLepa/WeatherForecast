package com.example.weatherforecast.db.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.db.entity.weather.*
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeathersListResponse(
    val cnt: Int,
    @SerializedName("list")
    val weathers: List<WeatherResponse>
) : Parcelable