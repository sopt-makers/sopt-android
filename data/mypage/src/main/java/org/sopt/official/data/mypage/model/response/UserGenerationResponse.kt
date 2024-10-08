package org.sopt.official.data.mypage.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserGenerationResponse(
    @SerialName("currentGeneration")
    val currentGeneration: Int,
    @SerialName("status")
    val status: String,
) {
    fun toDomain() = currentGeneration
}
