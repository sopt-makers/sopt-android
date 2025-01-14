package org.sopt.official.domain.auth.model

data class VerificationResult(
    val isVerified: Boolean,
    val name: String,
    val phone: String
)