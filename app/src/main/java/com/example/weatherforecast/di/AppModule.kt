package com.example.weatherforecast.di

import android.app.Application
import androidx.room.Room
import com.example.weatherforecast.api.OpenWeatherApiService
import com.example.weatherforecast.db.WeatherForecastDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
object AppModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideWeatherForecastDatabase(app: Application): WeatherForecastDatabase {
        return Room
            .databaseBuilder(
                app,
                WeatherForecastDatabase::class.java,
                "weather_forecast_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideWeatherDao(db: WeatherForecastDatabase) = db.weatherDao()

    @Singleton
    @Provides
    @JvmStatic
    fun provideOpenWeatherApiService() = OpenWeatherApiService()
}