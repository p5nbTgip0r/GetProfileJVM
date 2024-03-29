package cc.insulin.getprofile

import cc.insulin.getprofile.nightscout.data.ProfileChange
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object Parser {
    val objectMapper: ObjectMapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun parseProfileChanges(input: String): List<ProfileChange>? {
        return objectMapper.readValue(input)
    }
}