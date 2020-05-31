package cc.insulin.getprofile.oaps.data.profile

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ISFProfile(
        val sensitivities: List<SensitivityEntry>
)