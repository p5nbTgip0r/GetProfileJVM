package cc.insulin.getprofile.nightscout.data

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ProfileChange(
        @JsonProperty("startDate")
        val startDate: String,
        @JsonProperty("defaultProfile")
        val defaultProfile: String,
        @JsonProperty("store")
        val store: HashMap<String, NSProfile>,
        @JsonProperty("units")
        val units: String,
        @JsonProperty("created_at")
        val createdAt: String,
        @JsonProperty("millis")
        val millis: Long
)