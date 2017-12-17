package com.codingdojoangola.ui.blog

import java.util.*

/**
 * Created by pedromassango on 12/17/17.
 */
class TimeUtil {
    // TIME AGO
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    fun join(vararg args: Any): String {
        var joineed = ""
        for (s in args) {
            joineed = joineed + "" + s
        }
        return joineed
    }

    fun getTimeAgo(time: Long): String? {
        var time = time

        val MIN = " min"
        val HR = "h"
        val HA = "há"

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time <= 0) {
            return null
        } else if (time > now) {
            time = now
        }

        val diff = now - time
        if (diff < MINUTE_MILLIS) {
            return "agora"
        } else if (diff < 2 * MINUTE_MILLIS) {
            return concat(HA, join("1 ", MIN))
        } else if (diff < 50 * MINUTE_MILLIS) {
            return concat(HA, join(diff / MINUTE_MILLIS, MIN))
        } else if (diff < 90 * MINUTE_MILLIS) {
            return concat(HA, join("1 ", HR))
        } else if (diff < 24 * HOUR_MILLIS) {
            return concat(HA, join(diff / HOUR_MILLIS, HR))
        } else if (diff < 48 * HOUR_MILLIS) {
            return concat(HA, "1 d")
        } else if (diff / DAY_MILLIS > 30) {
            val c = Calendar.getInstance()
            c.timeInMillis = time
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH) + 1
            val year = c.get(Calendar.YEAR)
            return String.format("%s/%s/%s", day, month, year)
        } else {
            return concat(HA, join(diff / DAY_MILLIS, "d"))
        }
    }

    fun concat(vararg args: Any): String {
        var joineed = ""
        for (s in args) {
            joineed = joineed + " " + s
        }
        return joineed
    }
}