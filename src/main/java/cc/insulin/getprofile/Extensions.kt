package cc.insulin.getprofile

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Int.formatMinutes(): String {
    val seconds: Long = (this * 60).toLong()
    return LocalTime.ofSecondOfDay(seconds).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}