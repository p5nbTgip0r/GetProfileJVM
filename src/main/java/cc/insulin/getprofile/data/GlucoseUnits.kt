package cc.insulin.getprofile.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue


enum class GlucoseUnits {
    MGDL, MMOL;

    @JsonCreator
    fun forValue(value: String?): GlucoseUnits? {
        // use empty string if null
        val input = value?.toLowerCase() ?: ""
        val unit = with(input) {
            when {
                contains("mmol") -> MMOL
                else -> MGDL
            }
        }

        return unit
    }

    @JsonValue
    fun toValue(): String {
        return when (this) {
            MGDL -> "mg/dl"
            MMOL -> "mmol"
        }
    }
}