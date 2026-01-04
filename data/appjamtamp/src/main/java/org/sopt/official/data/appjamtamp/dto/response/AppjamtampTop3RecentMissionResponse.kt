package org.sopt.official.data.appjamtamp.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppjamtampTop3RecentMissionResponse(
    @SerialName("ranks")
    val ranks: List<AppjamtampRecentMissionResponse>
)

@Serializable
data class AppjamtampRecentMissionResponse(
    @SerialName("stampId")
    val stampId: Long,

    @SerialName("missionId")
    val missionId: Long,

    @SerialName("userId")
    val userId: Long,

    @SerialName("imageUrl")
    val imageUrl: String,

    @SerialName("createdAt")
    val createdAt: String?,

    @SerialName("userName")
    val userName: String,

    @SerialName("userProfileImage")
    val userProfileImage: String?,

    @SerialName("teamName")
    val teamName: String,

    @SerialName("teamNumber")
    val teamNumber: String
)