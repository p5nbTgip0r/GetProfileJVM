package cc.insulin.getprofile.nightscout

import cc.insulin.getprofile.nightscout.data.Nightscout
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object NightscoutRequest {
    private val client = OkHttpClient()

    fun getNightscoutProfile(ns: Nightscout): Response {
        val url = ns.url.toHttpUrl().newBuilder()
                .addPathSegments("api/v1/profile.json")
                .build()

        val request = Request.Builder()
                .url(url)
                .addAuth(ns.authentication)
                .get()
                .build()

        return client.newCall(request).execute()
    }
}

sealed class NightscoutResponse {
    data class Success(val response: String) : NightscoutResponse()
    data class Unauthorized(val message: String? = null) : NightscoutResponse()
    data class Error(val cause: Exception? = null) : NightscoutResponse()
}