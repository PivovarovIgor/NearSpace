package ru.brauer.nearspace.util

import java.util.*

const val MILLISECONDS_OF_ONE_DAY = 86_400_000

fun getYesterdayDate(): Long {
    val today = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"))
    today.set(Calendar.MILLISECOND, 0)
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.HOUR_OF_DAY, 0)
    return today.timeInMillis - MILLISECONDS_OF_ONE_DAY
}

fun getBeforeYesterday() = getYesterdayDate() - MILLISECONDS_OF_ONE_DAY
