package org.sopt.official.data.auth.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CertificateCodeResponse(
    @SerialName("isVerified")
    val isVerified: Boolean,
    @SerialName("name")
    val name: String?,
    @SerialName("phone")
    val phone: String
)