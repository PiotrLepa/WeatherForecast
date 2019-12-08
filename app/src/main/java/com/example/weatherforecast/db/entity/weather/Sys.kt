package com.example.weatherforecast.db.entity.weather

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Sys(
    val country: String,
    val id: Int,
    val message: Double,
    val sunrise: Long,
    val sunset: Long,
    val type: Int
) : Parcelable