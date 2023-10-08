package org.sopt.official.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogOutResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String
)