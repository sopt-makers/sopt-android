package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileMessageResponse(
    @SerialName("profileMessage")
    val profileMessage: String
)
