package com.example.weatherforecast.ui.view

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.random.Random

class WeatherSingleView(
    color: Int,
    private var mLastX: Float,
    private var mLastY: Float,
    private val mRadius: Float,
    private val mWidth: Int
) {

    private val mPaint = Paint()
    private var mSpeedX = 0.25f
    private var mSpeedY = 0.25f
    private var mMaxViewMoveX: Float = 0.toFloat()
    private var mMaxViewMoveY: Float = 0.toFloat()
    private var mViewMoveSumX = 0f
    private var mViewMoveSumY = 0f

    init {
        mPaint.color = color
        init()
    }

    private fun init() {
        mPaint.alpha = 100
        mMaxViewMoveX = calculateMaxViewMove()
        mMaxViewMoveY = calculateMaxViewMove()
    }

    fun move(canvas: Canvas, translationX: Float, translationY: Float) {
        canvas.drawCircle(mLastX + translationX, mLastY + translationY, mRadius, mPaint)

        mLastY += mSpeedY
        mLastX += mSpeedX
        mViewMoveSumX += Math.abs(mSpeedX)
        mViewMoveSumY += Math.abs(mSpeedY)
        if (shouldChangeDirection(mMaxViewMoveX, mViewMoveSumX)) {
            if (mSpeedX <= 0) mMaxViewMoveX = calculateMaxViewMove()
            mViewMoveSumX = 0f
            mSpeedX *= -1f
        }
        if (shouldChangeDirection(mMaxViewMoveY, mViewMoveSumY)) {
            if (mSpeedY <= 0) mMaxViewMoveY = calculateMaxViewMove()
            mViewMoveSumY = 0f
            mSpeedY *= -1f
        }
    }

    private fun shouldChangeDirection(max: Float, currentValue: Float): Boolean {
        return currentValue >= max
    }

    private fun calculateMaxViewMove(): Float {
        return (mWidth / 40 * (Random.nextInt(3) + 1)).toFloat()
    }
}