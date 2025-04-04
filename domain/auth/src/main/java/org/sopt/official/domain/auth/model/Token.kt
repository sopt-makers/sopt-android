package org.sopt.official.domain.auth.model

data class Token(
    val accessToken: String,
    val refreshToken: String
)