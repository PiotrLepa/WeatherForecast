package com.example.weatherforecast.util

import android.content.Context
import com.example.weatherforecast.R
import com.example.weatherforecast.db.entity.City
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets


class JsonFileReader(
    private val context: Context
) {

    fun loadCitiesFromResources(): List<City> {
        val inputStream = context.resources.openRawResource(R.raw.openweathermap_cities_list_pl)
        val reader = JsonReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
        val cities = Gson().fromJson<List<City>>(reader)
        reader.close()
        return cities
    }
}