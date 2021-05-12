package cc.insulin.getprofile.command

import cc.insulin.getprofile.Parser
import cc.insulin.getprofile.nightscout.NightscoutRequest
import cc.insulin.getprofile.nightscout.NightscoutResponse
import cc.insulin.getprofile.nightscout.data.Nightscout
import org.apache.logging.log4j.kotlin.Logging
import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(name = "print",
        description = ["Prints the current Nightscout profile to console"],
        mixinStandardHelpOptions = true,
        showDefaultValues = true)
class PrintCommand : Callable<Int>, Logging {
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
                logger.info(defaultProfile)
            }
            is NightscoutResponse.Unauthorized -> {
                logger.fatal("Authorization is not valid")
                return 1
            }
            is NightscoutResponse.Error -> {
                logger.fatal("Error occurred while fetching NS profile")
                throw response.cause ?: Exception()
            }
        }

        return 0
    }
}