package com.example.weatherforecast.ui.addCity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.db.entity.City
import timber.log.Timber

class CitiesAdapter(
    private val citiesList: List<City>
) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = citiesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = citiesList[position].name
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cityName: TextView = view.findViewById(R.id.cityNameText)
    }
}