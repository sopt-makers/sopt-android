package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.SoptampUser

@Serializable
data class UserResponse(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("points")
    val points: Int,
    @SerialName("profileMessage")
    val profileMessage: String? = null,
) {
    fun toDomain() = SoptampUser(nickname, points, profileMessage ?: "")
}
