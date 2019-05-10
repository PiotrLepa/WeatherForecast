package com.example.weatherforecast.util

class DateUtils {

    companion object {
        fun elapsedMinutesSince(fromTime: String): Long {
            val timeDifferenceInSeconds = System.currentTimeMillis() / 1000 - java.lang.Long.valueOf(fromTime)
            return timeDifferenceInSeconds / 60
        }
    }
}