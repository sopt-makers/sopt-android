package org.sopt.official.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NonDataBaseAuthResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    val data: String?
)