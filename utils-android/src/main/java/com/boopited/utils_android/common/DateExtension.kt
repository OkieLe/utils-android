package com.boopited.utils_android.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT_DATE_NO_TIME_ENGLISH = "yyyy-MM-dd"

fun Date.now() = Date()
fun Date.timestamp() = time

fun Date.today() = Date()
fun Date.tomorrow() = addDay(1)
fun Date.yesterday() = addDay(-1)

fun Date.of(year: Int = -1, month: Int = -1, day: Int = -1,
            hour: Int = -1, minute: Int = -1, second: Int = -1): Date {
    val calendar = Calendar.getInstance()
    calendar.takeIf { year > 0 }?.apply { calendar.set(Calendar.YEAR, year) }
    calendar.takeIf { month > 0 }?.apply { calendar.set(Calendar.MONTH, month - 1) }
    calendar.takeIf { day > 0 }?.apply { calendar.set(Calendar.DAY_OF_MONTH, day) }
    calendar.takeIf { hour > 0 }?.apply { calendar.set(Calendar.HOUR_OF_DAY, hour) }
    calendar.takeIf { minute > 0 }?.apply { calendar.set(Calendar.MINUTE, minute) }
    calendar.takeIf { second > 0 }?.apply { calendar.set(Calendar.SECOND, second) }
    return calendar.time
}

fun Date.from1970(year: Int = -1, month: Int = -1, day: Int = -1,
                  hour: Int = -1, minute: Int = -1, second: Int = -1): Date {
    val calendar = Calendar.getInstance()
    calendar.set(1970, 0, 1, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.takeIf { year > 0 }?.apply { calendar.set(Calendar.YEAR, year + 1970) }
    calendar.takeIf { month > 0 }?.apply { calendar.set(Calendar.MONTH, month) }
    calendar.takeIf { day > 0 }?.apply { calendar.set(Calendar.DAY_OF_MONTH, day + 1) }
    calendar.takeIf { hour > 0 }?.apply { calendar.set(Calendar.HOUR_OF_DAY, hour) }
    calendar.takeIf { minute > 0 }?.apply { calendar.set(Calendar.MINUTE, minute) }
    calendar.takeIf { second > 0 }?.apply { calendar.set(Calendar.SECOND, second) }
    calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.ZONE_OFFSET))
    return calendar.time
}

fun Long.toDate() = Date(this)

fun Date.minus(date: Date) = Math.abs(this.time - date.time)

fun Date.lessThan(date: Date) = this.time - date.time < 0

fun Date.greaterThan(date: Date) = this.time - date.time > 0

fun Date.greaterEqualThan(date: Date) = this.time - date.time >= 0

fun Date.englishDateNoTime():String = SimpleDateFormat(DATE_FORMAT_DATE_NO_TIME_ENGLISH, Locale.CHINA).format(this)

fun fullEnglishNoTimeDate(dateString: String): Date {
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT_DATE_NO_TIME_ENGLISH, Locale.CHINA)
    return if (dateString.isEmpty()) Date() else {
        try {
            simpleDateFormat.parse(dateString)
        } catch (e: ParseException) {
            Date()
        }
    }
}

fun Date.addYear(value: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, value)
    return calendar.time
}

fun Date.addDay(value: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, value)
    return calendar.time
}