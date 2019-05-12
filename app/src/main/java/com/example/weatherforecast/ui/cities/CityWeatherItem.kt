package com.example.weatherforecast.ui.cities

import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.formatTemperature
import com.example.weatherforecast.util.WeatherUnitUtils.Companion.getWeatherConditionIconUrl
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_city_weather.*

class CityWeatherItem(val weather: WeatherResponse) : Item() {

    override fun getLayout() = R.layout.item_city_weather

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.cityNameText.text = weather.name
        viewHolder.temperatureText.text = formatTemperature(weather.main.temp)

        Glide.with(viewHolder.weatherConditionImage)
            .load(getWeatherConditionIconUrl(weather.weather[0].icon))
            .into(viewHolder.weatherConditionImage)
    }
}