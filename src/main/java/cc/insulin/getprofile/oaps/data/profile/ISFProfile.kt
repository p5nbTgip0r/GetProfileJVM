package cc.insulin.getprofile.oaps.data.profile

import cc.insulin.getprofile.data.GlucoseUnits
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ISFProfile(
        val sensitivities: List<SensitivityEntry>,
        val units: GlucoseUnits,
        @JsonProperty("user_preferred_units")
        val userPreferredUnits: GlucoseUnits = units
)