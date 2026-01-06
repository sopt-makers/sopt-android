package org.sopt.official.domain.appjamtamp.entity

data class AppjamtampRecentMission(
    val stampId: Long,
    val missionId: Long,
    val userId: Long,
    val imageUrl: String,
    val createdAt: String?,
    val userName: String,
    val userProfileImage: String?,
    val teamName: String,
    val teamNumber: String
)
