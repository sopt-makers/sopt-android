package org.sopt.official.data.poke.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeMessageRequest(
    @SerialName("isAnonymous")
    val isAnonymous: Boolean,
    @SerialName("message")
    val message: String
)
