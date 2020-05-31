package cc.insulin.getprofile.oapsdata.profile

import cc.insulin.getprofile.formatMinutes
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder("i", "start", "sensitivity", "offset", "x", "endOffset")
data class SensitivityEntry(
        @JsonProperty("i")
        val iterator: Int,
        val sensitivity: Int,
        val offset: Int,
        val x: Int,
        val endOffset: Int,
        val start: String = offset.formatMinutes()
)