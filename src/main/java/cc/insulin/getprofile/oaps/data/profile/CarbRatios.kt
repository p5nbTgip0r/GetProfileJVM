package cc.insulin.getprofile.oaps.data.profile

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class CarbRatios(
        @JsonProperty("schedule")
        val carbSchedule: List<ScheduleEntry>,
        val units: String = "grams"
) {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonPropertyOrder("x", "i", "offset", "ratio", "r", "start")
    data class ScheduleEntry(
            val ratio: Double,
            val start: String,
            val r: Double = ratio,
            val offset: Int = 0,
            val x: Int = 0,
            val i: Int = 0
    )
}