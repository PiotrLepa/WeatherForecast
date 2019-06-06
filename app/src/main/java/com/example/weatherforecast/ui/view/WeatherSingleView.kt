package com.example.weatherforecast.ui.view

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.random.Random

class WeatherSingleView(
    color: Int,
    private val isMoveable: Boolean,
    private var x: Float,
    private var y: Float,
    private val radius: Float,
    private val width: Int
) {

    private val paint = Paint()
    private var speedX = 0.25f
    private var speedY = 0.25f
    private var maxViewMoveX: Float = 0.toFloat()
    private var maxViewMoveY: Float = 0.toFloat()
    private var viewMoveSumX = 0f
    private var viewMoveSumY = 0f

    init {
        paint.color = color
        init()
    }

    private fun init() {
        paint.alpha = 150
        maxViewMoveX = calculateMaxViewMove()
        maxViewMoveY = calculateMaxViewMove()
    }

    fun move(canvas: Canvas, translationX: Float, translationY: Float) {
        canvas.drawCircle(x + translationX, y + translationY, radius, paint)

        if (!isMoveable) return

        y += speedY
        x += speedX
        viewMoveSumX += Math.abs(speedX)
        viewMoveSumY += Math.abs(speedY)
        if (shouldChangeDirection(maxViewMoveX, viewMoveSumX)) {
            if (speedX <= 0) maxViewMoveX = calculateMaxViewMove()
            viewMoveSumX = 0f
            speedX *= -1f
        }
        if (shouldChangeDirection(maxViewMoveY, viewMoveSumY)) {
            if (speedY <= 0) maxViewMoveY = calculateMaxViewMove()
            viewMoveSumY = 0f
            speedY *= -1f
        }
    }

    private fun shouldChangeDirection(max: Float, currentValue: Float): Boolean {
        return currentValue >= max
    }

    private fun calculateMaxViewMove(): Float {
        return (width / 40 * (Random.nextInt(3) + 1)).toFloat()
    }
}