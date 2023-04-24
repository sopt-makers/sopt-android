package org.sopt.official.stamp.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.stamp.domain.model.SoptampUser

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
