package cc.insulin.getprofile.oapsdata.profile

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ISFProfile(
        val sensitivities: List<SensitivityEntry>
)