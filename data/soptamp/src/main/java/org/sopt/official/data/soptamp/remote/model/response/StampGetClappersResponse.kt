package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.StampClappers

@Serializable
data class StampGetClappersResponse(
    @SerialName("users")
    val clapUsers: List<ClapUserResponse>
) {
    @Serializable
    data class ClapUserResponse(
        @SerialName("nickname")
        val nickname: String,
        @SerialName("profileImageUrl")
        val profileImageUrl: String?,
        @SerialName("profileMessage")
        val profileMessage: String?,
        @SerialName("clapCount")
        val clapCount: Int
    ) {
        fun toDomain() = StampClappers.StampClapUser(
            nickname = nickname,
            profileImage = profileImageUrl ?: "",
            profileMessage = profileMessage ?: "",
            clapCount = clapCount
        )
    }

    fun toDomain() = StampClappers(
        clappers = clapUsers.map { it.toDomain() }
    )
}