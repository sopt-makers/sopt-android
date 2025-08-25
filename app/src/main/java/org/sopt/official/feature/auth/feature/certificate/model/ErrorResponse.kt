package org.sopt.official.feature.auth.feature.certificate.model

data class ErrorResponse(
    val success: Boolean,
    val message: String,
    val data: Any?
)
