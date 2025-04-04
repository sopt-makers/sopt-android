package org.sopt.official.auth.model

data class CentralizeToken(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun setExpiredToken() = CentralizeToken(
            accessToken = "",
            refreshToken = ""
        )
    }
}
