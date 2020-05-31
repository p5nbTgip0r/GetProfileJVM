package cc.insulin.getprofile

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Int.formatMinutes(): String {
    return LocalTime.of(0, this).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}