package org.sopt.official.domain.auth.model

data class SignInResult(
    val accessToken: String,
    val refreshToken: String
)