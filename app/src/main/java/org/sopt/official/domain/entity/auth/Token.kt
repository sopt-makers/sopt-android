package org.sopt.official.domain.entity.auth

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val playgroundToken: String
)
