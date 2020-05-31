package cc.insulin.getprofile.oapsdata

import cc.insulin.getprofile.oapsdata.profile.BasalEntry
import cc.insulin.getprofile.oapsdata.profile.ISFProfile
import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class OAPSProfile(
        val min_5m_carbimpact: Double,
        val dia: Double,
        val basalProfile: List<BasalEntry>,
        val isfProfile: ISFProfile,
        val carbRatio: Double,
        val autosensMax: Double,
        val autosensMin: Double
)