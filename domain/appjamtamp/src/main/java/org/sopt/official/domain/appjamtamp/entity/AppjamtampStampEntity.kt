package org.sopt.official.domain.appjamtamp.entity

data class AppjamtampStampEntity(
    val stampId: Int,
    val contents: String,
    val images: List<String>,
    val activityDate: String,
    val createdAt: String,
    val updatedAt: String,
    val missionId: Int,
    val teamNumber: String,
    val teamName: String,
    val ownerNickname: String,
    val ownerProfileImage: String?,
    val clapCount: Int,
    val viewCount: Int,
    val myClapCount: Int,
    val isMine: Boolean
)
