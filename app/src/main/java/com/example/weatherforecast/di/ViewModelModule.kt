package com.example.weatherforecast.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.ui.addCity.AddCityViewModel
import com.example.weatherforecast.ui.cities.CitiesViewModel
import com.example.weatherforecast.ui.weather.ForecastViewModel
import com.example.weatherforecast.ui.weather.WeatherViewModel
import com.example.weatherforecast.util.WeatherForecastViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: WeatherForecastViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddCityViewModel::class)
    abstract fun bindAddCityViewModel(addCityViewModel: AddCityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel::class)
    abstract fun bindCitiesViewModel(citiesViewModel: CitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel::class)
    abstract fun bindForecastViewModel(forecastViewModel: ForecastViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun bindWeatherViewModel(weatherViewModel: WeatherViewModel): ViewModel
}