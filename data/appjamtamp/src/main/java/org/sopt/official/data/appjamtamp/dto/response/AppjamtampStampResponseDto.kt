package org.sopt.official.data.appjamtamp.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppjamtampStampResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("contents")
    val contents: String,
    @SerialName("images")
    val images: List<String>,
    @SerialName("activityDate")
    val activityDate: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("missionId")
    val missionId: Int,
    @SerialName("teamNumber")
    val teamNumber: String,
    @SerialName("teamName")
    val teamName: String,
    @SerialName("ownerNickname")
    val ownerNickname: String,
    @SerialName("ownerProfileImage")
    val ownerProfileImage: String?,
    @SerialName("clapCount")
    val clapCount: Int,
    @SerialName("viewCount")
    val viewCount: Int,
    @SerialName("myClapCount")
    val myClapCount: Int,
    @SerialName("isMine")
    val isMine: Boolean
)
