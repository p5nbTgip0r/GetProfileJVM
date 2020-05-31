package cc.insulin.getprofile.oapsdata.profile

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class SensitivityEntry(
        @JsonProperty("i")
        val iterator: Int,
        val start: String,
        val sensitivity: Int,
        val offset: Int,
        val x: Int,
        val endOffset: Long
)