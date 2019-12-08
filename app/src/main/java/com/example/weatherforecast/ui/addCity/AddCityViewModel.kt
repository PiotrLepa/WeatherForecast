package com.example.weatherforecast.ui.addCity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.db.entity.City
import com.example.weatherforecast.util.JsonFileReader
import java.util.ArrayList
import javax.inject.Inject
import java.text.Normalizer


class AddCityViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    }

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
        val normalizedQuery = query.toLowerCaseAndUnaccent()
        val filteredCityList = ArrayList<City>()
        for (city in models) {
            val normalizedCityName = city.name.toLowerCaseAndUnaccent()
            if (normalizedCityName.contains(normalizedQuery)) {
                filteredCityList.add(city)
            }
        }
        return filteredCityList
    }

    private fun String.toLowerCaseAndUnaccent(): String {
        val normalizer = Normalizer
            .normalize(this.toLowerCase(), Normalizer.Form.NFD)
        return REGEX_UNACCENT.replace(normalizer, "")
    }
}
