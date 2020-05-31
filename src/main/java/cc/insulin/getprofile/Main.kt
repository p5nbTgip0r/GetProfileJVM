package cc.insulin.getprofile

import cc.insulin.getprofile.nsdata.ProfileChange
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

fun main() {
    get()
}

fun get() {
    val client = OkHttpClient()
    val url = HttpUrl.Builder()
            .scheme("https")
            .host("ns.insulin.cc")
            .addPathSegments("api/v1/profile.json")
            .addQueryParameter("token", System.getenv("TOKEN"))
            .build()

    val request = Request.Builder()
            .url(url)
            .get()
            .build()

    val response = client.newCall(request).execute()

    val responseBody = response.body!!.string()

    val mapperAll = ObjectMapper().registerModule(KotlinModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val objData = mapperAll.readTree(responseBody).get(0).toPrettyString()
    println(objData)
    val profileChange: ProfileChange? = mapperAll.readValue(objData)
    println(profileChange!!)
}