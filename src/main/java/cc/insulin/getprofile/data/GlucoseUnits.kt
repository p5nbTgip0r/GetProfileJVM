package cc.insulin.getprofile.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*


enum class GlucoseUnits {
    MGDL, MMOL;

    @JsonCreator
    fun forValue(value: String?): GlucoseUnits {
        // use empty string if null
        val input = value?.lowercase(Locale.getDefault()) ?: ""
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