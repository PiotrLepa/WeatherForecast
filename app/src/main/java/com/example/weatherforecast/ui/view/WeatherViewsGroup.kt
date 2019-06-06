package com.example.weatherforecast.ui.view

import android.graphics.Canvas
import android.graphics.Color
import timber.log.Timber

class WeatherViewsGroup(
    private val width: Int,
    private val height: Int) {

    private var weatherViews = ArrayList<WeatherSingleView>()
    private val maxViewMoveSensorX: Float = (width / 5).toFloat()
    private val maxViewMoveSensorY: Float = (height / 5).toFloat()

    init {
        weatherViews = createCloudyWeatherViews()
    }

    fun move(canvas: Canvas, sensorX: Float, sensorY: Float) {
        val translationX = maxViewMoveSensorX * (sensorX / MAX_SENSORS_VALUE)
        val translationY = maxViewMoveSensorY * (sensorY / MAX_SENSORS_VALUE)
        for (view in weatherViews) {
            view.move(canvas, translationX, translationY)
        }
    }

    private fun createCloudyWeatherViews(): ArrayList<WeatherSingleView> {
        val weatherViews = ArrayList<WeatherSingleView>()
        
        Timber.d("createCloudyWeatherViews: height: $height")
        weatherViews.add(WeatherSingleView(Color.WHITE, true, width - 120f,  -width / 3.5f, width / 3.3f, width)) // right bigger
        weatherViews.add(WeatherSingleView(Color.WHITE, true, width - 150f,  width / 5f, width / 4f, width)) // right small
        weatherViews.add(WeatherSingleView(Color.WHITE, true, -width / 25f, -height / 10f, width / 2.4f, width)) // left edge
        weatherViews.add(WeatherSingleView(Color.WHITE, true,width / 2f, -height / 100f, width / 2.5f, width)) // center big
        weatherViews.add(WeatherSingleView(Color.WHITE, true, width / 4f, -height / 7f, width / 2.2f, width)) // left from edge big
        weatherViews.add(WeatherSingleView(Color.WHITE, true, -width / 7f, -height / 2f, width / 1.5f, width)) // left edge biggest
        weatherViews.add(WeatherSingleView(Color.WHITE, true, width / 4f * 3, -height / 5f, width / 2.5f, width)) // center/right bigger
        weatherViews.add(WeatherSingleView(Color.WHITE, true, width / 4f * 3 + 50, -height / 3.2f, width / 2.1f, width)) // center/right smaller

        weatherViews.add(WeatherSingleView(Color.rgb(255, 200, 0), false, width / 3f * 2, height / 3.5f, width / 8f, width))
        
        return weatherViews
    }

    companion object {
        private const val MAX_SENSORS_VALUE = 10
    }
}