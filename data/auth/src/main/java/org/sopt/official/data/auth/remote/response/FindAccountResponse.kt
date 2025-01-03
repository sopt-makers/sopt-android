package org.sopt.official.data.auth.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindAccountResponse(
    @SerialName("platform")
    val platform: String
)