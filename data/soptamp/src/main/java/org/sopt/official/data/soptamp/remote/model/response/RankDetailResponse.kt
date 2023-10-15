package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.OtherMission

@Serializable
data class RankDetailResponse(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileMessage")
    val profileMessage: String?,
    @SerialName("userMissions")
    val userMissions: List<UserMissionResponse>
) {
    @Serializable
    data class UserMissionResponse(
        @SerialName("display")
        val display: Boolean,
        @SerialName("id")
        val id: Int,
        @SerialName("level")
        val level: Int,
        @SerialName("profileImage")
        val profileImage: String?,
        @SerialName("title")
        val title: String
    ) {
        fun toEntity() = OtherMission.Mission(
            display = display,
            id = id,
            level = level,
            profileImage = profileImage,
            title = title,
        )
    }

    fun toEntity() = OtherMission(
        nickname = nickname,
        profileMessage = profileMessage ?: "",
        userMissions = userMissions.map { it.toEntity() }
    )
}