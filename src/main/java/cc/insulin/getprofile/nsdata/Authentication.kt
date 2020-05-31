package cc.insulin.getprofile.nsdata

data class Authentication(val auth: String,
                          val type: AuthType = if (auth.startsWith("token=")) AuthType.TOKEN else AuthType.API_SECRET)

enum class AuthType {
    API_SECRET, TOKEN
}