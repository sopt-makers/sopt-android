package org.sopt.official.data.appjamtamp.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppjamtampTop10MissionScoreResponse(
    @SerialName("ranks")
    val ranks: List<AppjamtampMissionScoreResponse>
)

@Serializable
data class AppjamtampMissionScoreResponse(
    @SerialName("rank")
    val rank: Int,
    @SerialName("teamName")
    val teamName: String,
    @SerialName("teamNumber")
    val teamNumber: String,
    @SerialName("todayPoints")
    val todayPoints: Int,
    @SerialName("totalPoints")
    val totalPoints: Int
)
