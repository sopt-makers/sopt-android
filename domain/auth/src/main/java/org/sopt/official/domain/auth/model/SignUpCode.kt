package org.sopt.official.domain.auth.model

data class SignUpCode(
    val name: String,
    val phone: String,
    val code: String,
    val authPlatform: String
)
