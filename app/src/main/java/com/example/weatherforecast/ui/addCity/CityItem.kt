package com.example.weatherforecast.ui.addCity

import com.example.weatherforecast.R
import com.example.weatherforecast.db.entity.City
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_city.*

class CityItem(val city: City) : Item() {

    override fun getLayout() = R.layout.item_city

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.cityNameText.text = city.name
    }
}