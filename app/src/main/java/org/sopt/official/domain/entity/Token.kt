package org.sopt.official.domain.entity

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val playgroundToken: String
)
