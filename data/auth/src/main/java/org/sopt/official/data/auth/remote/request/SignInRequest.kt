package org.sopt.official.data.auth.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest (
    @SerialName("code")
    val code: String,
    @SerialName("authPlatform")
    val authPlatform: String
)

