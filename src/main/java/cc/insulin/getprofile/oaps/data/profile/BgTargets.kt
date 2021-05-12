package cc.insulin.getprofile.oaps.data.profile

import cc.insulin.getprofile.data.GlucoseUnits
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class BgTargets(
        val first: Int = 1,
        val targets: List<Target>,
        @JsonProperty("user_preferred_units")
        val userPreferredUnits: GlucoseUnits,
        val units: GlucoseUnits = userPreferredUnits
) {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonPropertyOrder("max_bg", "min_bg", "x", "offset", "low", "start", "high", "i")
    data class Target(
            @JsonProperty("max_bg")
            val maxBg: Double,
            @JsonProperty("min_bg")
            val minBg: Double,
            val start: String,
            val low: Double = minBg,
            val high: Double = maxBg,
            val offset: Int = 0,
            val x: Int = 0,
            val i: Int = 0
    )
}