package cc.insulin.getprofile.nightscout

import cc.insulin.getprofile.addAuth
import cc.insulin.getprofile.nightscout.data.Nightscout
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.logging.log4j.kotlin.Logging

object NightscoutRequest : Logging {
    private val client = OkHttpClient()

    fun fetchNightscoutProfile(ns: Nightscout): NightscoutResponse {
        logger.debug("Fetching NS profile $ns")
        return getNightscoutResponse(ns.getProfileURL(), ns.authentication)
    }

    fun getNightscoutResponse(url: HttpUrl, auth: String? = null): NightscoutResponse {
        return try {
            val response: Response = doNightscoutRequest(url, auth)
            logger.debug("Got response: $response")
            if (response.code == 401) {
                logger.debug("Unauthorized")
                NightscoutResponse.Unauthorized(response.body?.string())
            } else {
                NightscoutResponse.Success(response.body!!.string())
            }
        } catch (e: Exception) {
            NightscoutResponse.Error(e)
        }
    }

    fun doNightscoutRequest(url: HttpUrl, auth: String? = null): Response {
        val request = Request.Builder()
                .url(url)
                .addAuth(auth)
                .get()
                .build()

        logger.debug("Sending NS request: $request")

        return client.newCall(request).execute()
    }
}

sealed class NightscoutResponse {
    data class Success(val response: String) : NightscoutResponse()
    data class Unauthorized(val message: String? = null) : NightscoutResponse()
    data class Error(val cause: Exception? = null) : NightscoutResponse()
}