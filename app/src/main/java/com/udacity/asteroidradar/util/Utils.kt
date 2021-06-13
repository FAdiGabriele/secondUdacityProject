package com.udacity.asteroidradar.util

import java.text.SimpleDateFormat
import java.util.*

fun getActualDateFormatted() : String{
    val now = Date().time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(now)
}

fun getTheNextSeventhDayFormatted() : String{
    val sevenDays = 1000*60*60*24*7
    val nextWeek = Date().time + sevenDays
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(nextWeek)
}
