package org.sopt.official.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LogOutResponse(
    val status: Int,
    val success: Boolean,
    val message: String
)