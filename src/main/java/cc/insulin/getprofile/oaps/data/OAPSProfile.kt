package cc.insulin.getprofile.oaps.data

import cc.insulin.getprofile.data.GlucoseUnits
import cc.insulin.getprofile.oaps.data.profile.BasalEntry
import cc.insulin.getprofile.oaps.data.profile.BgTargets
import cc.insulin.getprofile.oaps.data.profile.CarbRatios
import cc.insulin.getprofile.oaps.data.profile.ISFProfile
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class OAPSProfile(
        @JsonProperty("min_5m_carbimpact")
        val min5mCarbImpact: Double,
        val dia: Double,
        @JsonProperty("basalprofile")
        val basalProfile: List<BasalEntry>,
        val isfProfile: ISFProfile,
        @JsonProperty("bg_targets")
        val bgTargets: BgTargets,
        @JsonProperty("carb_ratios")
        val carbRatios: CarbRatios,
        @JsonProperty("carb_ratio")
        val carbRatio: Double,
        @JsonProperty("autosens_max")
        val autosensMax: Double = 1.2,
        @JsonProperty("autosens_min")
        val autosensMin: Double = 0.7,
        @JsonProperty("out_units")
        val outUnits: GlucoseUnits,
        val curve: String = "rapid-acting",
        val useCustomPeakTime: Boolean = false,
        val insulinPeakTime: Int = 75,
        val timezone: String
)