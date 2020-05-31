package cc.insulin.getprofile

import cc.insulin.getprofile.nightscout.NightscoutRequest
import cc.insulin.getprofile.nightscout.data.Nightscout
import cc.insulin.getprofile.nightscout.data.ProfileChange
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(name = "print",
        mixinStandardHelpOptions = true,
        showDefaultValues = true)
class PrintCommand : Callable<Int> {
    @CommandLine.Mixin
    lateinit var options: Options

    override fun call(): Int {
        val ns = Nightscout(options.nsUrl, options.auth)

        val response = NightscoutRequest.getNightscoutProfile(ns)

        val responseBody = response.body!!.string()

        val mapperAll = ObjectMapper().registerModule(KotlinModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val objData = mapperAll.readTree(responseBody).get(0).toPrettyString()
        println(objData)
        val profileChange: ProfileChange? = mapperAll.readValue(objData)
        println(profileChange!!)
        return 1
    }
}