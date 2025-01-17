package org.sopt.official.data.auth.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeAccountRequest (
    @SerialName("phone")
    val phone: String,
    @SerialName("authPlatform")
    val authPlatform: String,
    @SerialName("token")
    val token: String,
)