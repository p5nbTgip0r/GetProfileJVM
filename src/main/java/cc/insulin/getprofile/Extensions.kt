package cc.insulin.getprofile

import okhttp3.Request
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Int.formatMinutes(): String {
    val seconds: Long = (this * 60).toLong()
    return LocalTime.ofSecondOfDay(seconds).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}


fun Request.Builder.addAuth(auth: String?): Request.Builder {
    if (auth != null) {
        return this.addHeader("api-secret", auth)
    }
    return this
}