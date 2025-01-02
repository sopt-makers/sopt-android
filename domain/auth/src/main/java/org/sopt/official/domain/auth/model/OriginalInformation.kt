package org.sopt.official.domain.auth.model

data class OriginalInformation(
    val phone: String,
    val authPlatform: String,
    val code: String,
)