package cc.insulin.getprofile

import cc.insulin.getprofile.nightscout.NightscoutRequest
import cc.insulin.getprofile.nightscout.NightscoutResponse
import cc.insulin.getprofile.nightscout.data.Nightscout
import cc.insulin.getprofile.oaps.OAPSConverter
import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(name = "convert",
        description = ["Converts the current NS profile into an OpenAPS-formatted profile"],
        mixinStandardHelpOptions = true,
        showDefaultValues = true)
class ConvertCommand : Callable<Int>, Logging {
    @CommandLine.Mixin
    lateinit var options: Options

    override fun call(): Int {
        val ns = Nightscout(options.nsUrl, options.auth)

        when (val response = NightscoutRequest.fetchNightscoutProfile(ns)) {
            is NightscoutResponse.Success -> {
                val responseBody = response.response

                val profileChanges = Parser.parseProfileChanges(responseBody)
                val profileChange = profileChanges!![0]
                val defaultProfile = profileChange.store[profileChange.defaultProfile]!!

                val oaps = OAPSConverter.convertProfile(defaultProfile)
                logger.info(Parser.objectMapper.writeValueAsString(oaps))
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