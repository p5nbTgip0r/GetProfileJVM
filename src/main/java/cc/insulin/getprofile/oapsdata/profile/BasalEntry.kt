package cc.insulin.getprofile.oapsdata.profile

import com.fasterxml.jackson.annotation.JsonAutoDetect

// todo change the values to have their proper types
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class BasalEntry(
        val start: String,
        val minutes: String,
        val rate: Double
)