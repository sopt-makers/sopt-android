package org.sopt.official.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAuthEmail(
    @SerialName("user_id") val userId: Long,
)
