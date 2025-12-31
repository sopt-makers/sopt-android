package org.sopt.official.data.appjamtamp.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity

@Serializable
data class AppjamtampMissionsResponse(
    @SerialName("teamNumber")
    val teamNumber: String,
    @SerialName("teamName")
    val teamName: String,
    @SerialName("missions")
    val missions: List<AppjamtampMissionItemDto>
)

@Serializable
data class AppjamtampMissionItemDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("ownerName")
    val ownerName: String,
    @SerialName("level")
    val level: Int,
    @SerialName("profileImage")
    val profileImage: List<String>,
    @SerialName("isCompleted")
    val isCompleted: Boolean
) {
    fun toEntity(): AppjamtampMissionEntity {
        return AppjamtampMissionEntity(
            id = id,
            title = title,
            ownerName = ownerName,
            level = level,
            profileImage = profileImage,
            isCompleted = isCompleted
        )
    }
}
