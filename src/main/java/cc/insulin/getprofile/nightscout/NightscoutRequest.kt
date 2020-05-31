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

fun Request.Builder.addAuth(auth: String?): Request.Builder {
    if (auth != null) {
        return this.addHeader("api-secret", auth)
    }
    return this
}