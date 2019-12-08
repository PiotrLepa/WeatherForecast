package com.example.weatherforecast.ui.view

import android.content.Context
import android.graphics.Canvas
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.AttributeSet
import android.view.View
import android.hardware.SensorManager
import android.os.Handler
import android.view.ViewTreeObserver


class WeatherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), SensorEventListener {


    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private lateinit var weatherViewGroup: WeatherViewsGroup
    private val threadHandler = Handler()
    private var sensorX: Float = 0.toFloat()
    private var sensorY: Float = 0.toFloat()

    init {
        val gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST)

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                weatherViewGroup = WeatherViewsGroup(width, height)
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            weatherViewGroup.move(canvas, sensorX, sensorY)
            threadHandler.postDelayed(
                { invalidate() },
                UI_REFRESH_DELAY
            )
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (Math.abs(sensorX-event.values[0]) > SENSORS_ACCURACY ||
            Math.abs(sensorY-event.values[1]) > SENSORS_ACCURACY) {
            sensorX = event.values[0]
            sensorY = event.values[1]
            invalidate()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    companion object {

        private val UI_REFRESH_DELAY = 40L //MILLISECONDS
        private val SENSORS_ACCURACY = 0.03 //minimal difference between old and new sensor read
    }
}