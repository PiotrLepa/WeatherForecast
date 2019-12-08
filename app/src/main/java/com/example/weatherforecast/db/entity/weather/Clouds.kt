package com.example.weatherforecast.db.entity.weather

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Clouds(
    val all: Double
) : Parcelable