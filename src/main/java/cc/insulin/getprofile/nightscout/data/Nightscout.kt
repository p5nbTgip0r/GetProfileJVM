package cc.insulin.getprofile.nightscout.data

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

data class Nightscout(val url: String,
                      val authentication: String? = null) {

    fun getProfileURL(): HttpUrl {
        return url.toHttpUrl().newBuilder()
                .addPathSegments("api/v1/profile.json")
                .build()
    }
}