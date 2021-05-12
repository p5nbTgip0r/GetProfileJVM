package cc.insulin.getprofile.command

import cc.insulin.getprofile.Parser
import cc.insulin.getprofile.nightscout.NightscoutRequest
import cc.insulin.getprofile.nightscout.NightscoutResponse
import cc.insulin.getprofile.nightscout.data.Nightscout
import cc.insulin.getprofile.oaps.OAPSConverter
import com.fasterxml.jackson.databind.ObjectWriter
import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine
import java.io.IOException
import java.util.concurrent.Callable

@CommandLine.Command(name = "convert",
        description = ["Converts the current NS profile into an OpenAPS-formatted profile"],
        mixinStandardHelpOptions = true,
        showDefaultValues = true)
class ConvertCommand : Callable<Int>, Logging {
    @CommandLine.Mixin
    lateinit var options: Options
    private val prettyPrinter: ObjectWriter = Parser.objectMapper
            .writerWithDefaultPrettyPrinter()

    override fun call(): Int {
        val ns = Nightscout(options.nsUrl, options.auth)

        when (val response = NightscoutRequest.fetchNightscoutProfile(ns)) {
            is NightscoutResponse.Success -> {
                val responseBody = response.response

                val profileChanges = Parser.parseProfileChanges(responseBody)
                val profileChange = profileChanges!![0]
                val defaultProfile = profileChange.store[profileChange.defaultProfile]!!

                val oaps = OAPSConverter.convertProfile(defaultProfile)
                val profileAsString = prettyPrinter.writeValueAsString(oaps)
                logger.info(profileAsString)
                if (options.outputFile != null) {
                    try {
                        prettyPrinter.writeValue(options.outputFile, oaps)
                        logger.info("Written profile to ${options.outputFile}")
                    } catch (e: IOException) {
                        logger.error("Could not write to file ${options.outputFile}", e)
                    }
                }
            }
            is NightscoutResponse.Unauthorized -> {
                logger.fatal("Authorization is not valid")
            }
            is NightscoutResponse.Error -> {
                logger.fatal("Error occurred while fetching NS profile")
                throw (response as? NightscoutResponse.Error)?.cause ?: Exception()
            }
        }

        return 1
    }
}