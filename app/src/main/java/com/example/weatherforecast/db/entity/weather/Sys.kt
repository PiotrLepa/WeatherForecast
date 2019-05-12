package com.example.weatherforecast.db.entity.weather

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Sys(
    val country: String,
    val id: Int,
    val message: Double,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
) : Parcelable