package com.example.weatherforecast.ui.addCity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.db.entity.City
import com.example.weatherforecast.util.JsonFileReader
import java.util.ArrayList
import javax.inject.Inject

class AddCityViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private lateinit var citiesList: List<City>

    private val _filteredCitiesList = MutableLiveData<List<City>>()
    val filteredCitiesList: LiveData<List<City>>
        get() = _filteredCitiesList

    init {
        loadCities()
    }

    fun queryCitiesList(query: String) {
        val filteredCities = filterCities(citiesList, query)
        _filteredCitiesList.value = filteredCities
    }

    private fun loadCities() {
        citiesList = JsonFileReader(getApplication()).loadCitiesFromResources()
        _filteredCitiesList.value = citiesList
    }

    private fun filterCities(models: List<City>, query: String): List<City> {
        val lowerCaseQuery = query.toLowerCase()
        val filteredCityList = ArrayList<City>()
        for (city in models) {
            val text = city.name.toLowerCase()
            if (text.contains(lowerCaseQuery)) {
                filteredCityList.add(city)
            }
        }
        return filteredCityList
    }
}
