package cc.insulin.getprofile.nightscout.data

import cc.insulin.getprofile.nightscout.data.profile.ScheduleEntry
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class NSProfile(
        val dia: Int,
        @JsonProperty("carbratio")
        val carbRatio: List<ScheduleEntry>,
        @JsonProperty("carbs_hr")
        val carbsPerHour: Int,
        val delay: Int,
        val sens: List<ScheduleEntry>,
        val timezone: String,
        val basal: List<ScheduleEntry>,
        @JsonProperty("target_low")
        val targetLow: List<ScheduleEntry>,
        @JsonProperty("target_high")
        val targetHigh: List<ScheduleEntry>,
        val startDate: String,
        val units: String = ""
)