package org.sopt.official.domain.auth.model

data class Auth(
    val token: String = "",
    val authPlatform: String = "",
    val refreshToken: String = "",
)
