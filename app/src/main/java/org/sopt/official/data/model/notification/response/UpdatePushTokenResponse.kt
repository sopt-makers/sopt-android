package org.sopt.official.data.model.notification.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePushTokenResponse(
    val status: Int,
    val success: Boolean,
    val message: String
)