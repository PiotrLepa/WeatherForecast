package com.example.weatherforecast.di

import com.example.weatherforecast.ui.addCity.AddCityFragment
import com.example.weatherforecast.ui.cities.CitiesFragment
import com.example.weatherforecast.ui.weather.ForecastFragment
import com.example.weatherforecast.ui.weather.WeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAddCityFragment(): AddCityFragment

    @ContributesAndroidInjector
    abstract fun contributeCitesFragment(): CitiesFragment

    @ContributesAndroidInjector
    abstract fun contributeForecastFragment(): ForecastFragment

    @ContributesAndroidInjector
    abstract fun contributeWeatherFragment(): WeatherFragment
}