package cc.insulin.getprofile.oapsdata.profile

import cc.insulin.getprofile.formatMinutes
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonPropertyOrder

// todo change the values to have their proper types
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder("start", "minutes", "rate")
data class BasalEntry(
        val minutes: Int,
        val rate: Double,
        val start: String = minutes.formatMinutes()
)