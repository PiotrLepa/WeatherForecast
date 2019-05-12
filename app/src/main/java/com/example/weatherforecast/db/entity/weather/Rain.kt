package com.example.weatherforecast.db.entity.weather


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rain(
    @SerializedName("3h")
    val h: Double
) : Parcelable