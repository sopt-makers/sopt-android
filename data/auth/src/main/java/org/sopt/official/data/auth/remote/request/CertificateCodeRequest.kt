package org.sopt.official.data.auth.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CertificateCodeRequest(
    @SerialName("name")
    val name: String?,
    @SerialName("phone")
    val phone: String,
    @SerialName("code")
    val code: String,
    @SerialName("type")
    val type: String,
)
