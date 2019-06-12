package com.example.weatherforecast

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

const val CITY_ID_SHARED_PREF = "CITY_ID_SHARED_PREF"
const val CITY_ID_ERROR_VALUE = -1

@Singleton
class CityProvider @Inject constructor(application: Application) {

    private val appContext = application.applicationContext

    private val sharedPref = appContext.getSharedPreferences(CITY_ID_SHARED_PREF, Context.MODE_PRIVATE)

    fun saveCityId(cityId: Int) {
        sharedPref.edit {
            putInt(CITY_ID_SHARED_PREF, cityId)
        }
    }

    fun getCityId(): Int? {
        val cityId = sharedPref.getInt(
            CITY_ID_SHARED_PREF,
            CITY_ID_ERROR_VALUE
        )
        return if (cityId != CITY_ID_ERROR_VALUE) {
            cityId
        } else {
            null
        }
    }
}