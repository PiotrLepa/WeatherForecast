package com.example.weatherforecast.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.db.entity.City
import com.example.weatherforecast.db.entity.WeatherForecastResponse
import com.example.weatherforecast.db.entity.WeatherResponse

@Database(
    entities = [
        WeatherResponse::class,
        City::class,
        WeatherForecastResponse::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListTypeConverter::class)
abstract class WeatherForecastDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
}