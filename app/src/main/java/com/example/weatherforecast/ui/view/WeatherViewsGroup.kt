package com.example.weatherforecast.ui.view

import android.graphics.Canvas
import android.graphics.Color

class WeatherViewsGroup(
    private val width: Int,
    private val height: Int) {

//    private val mViewsCount: Int = width / 10

    private var mWeatherViews = ArrayList<WeatherSingleView>()
    private val mMaxViewMoveSensorX: Float = (width / 3).toFloat()
    private val mMaxViewMoveSensorY: Float = (height / 3).toFloat()

    init {
        mWeatherViews = createCloudyWeatherViews()
    }

    fun move(canvas: Canvas, sensorX: Float, sensorY: Float) {
        val translationX = mMaxViewMoveSensorX * (sensorX / MAX_SENSORS_VALUE)
        val translationY = mMaxViewMoveSensorY * (sensorY / MAX_SENSORS_VALUE)
        for (view in mWeatherViews) {
            view.move(canvas, translationX, translationY)
        }
    }

    private fun createCloudyWeatherViews(): ArrayList<WeatherSingleView> {
        val weatherViews = ArrayList<WeatherSingleView>()
        weatherViews.add(WeatherSingleView(Color.WHITE, width / 4 * 3f, 20f, width / 3.4f, width))
        weatherViews.add(WeatherSingleView(Color.WHITE, width / 2f, 20f, width / 2.5f, width))
        weatherViews.add(WeatherSingleView(Color.WHITE, width / 2.5f, 54f, width / 2.8f, width))
        weatherViews.add(WeatherSingleView(Color.WHITE, -100f, -100f, width / 2.4f, width))
        weatherViews.add(WeatherSingleView(Color.WHITE, 50f, 50f, width / 3f, width))
        weatherViews.add(WeatherSingleView(Color.WHITE, width / 4f * 3, -150f, width / 2.9f, width))
        weatherViews.add(WeatherSingleView(Color.WHITE, width / 4f * 3 + 30, -190f, width / 2.7f, width))

        weatherViews.add(WeatherSingleView(Color.rgb(255, 200, 0), width / 3f * 2, height / 6f, width / 7f, width))

        return weatherViews
    }

    companion object {

        private const val MAX_SENSORS_VALUE = 10
    }
}