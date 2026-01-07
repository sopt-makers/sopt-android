package org.sopt.official.data.appjamtamp.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionListEntity

@Serializable
data class AppjamtampMissionsResponseDto(
    @SerialName("teamNumber")
    val teamNumber: String?,
    @SerialName("teamName")
    val teamName: String?,
    @SerialName("missions")
    val missions: List<AppjamtampMissionItemDto>
) {
    fun toEntity(): AppjamtampMissionListEntity {
        return AppjamtampMissionListEntity(
            teamNumber = teamNumber.orEmpty(),
            teamName = teamName.orEmpty(),
            missions = missions.map { it.toEntity() }
        )
    }
}

@Serializable
data class AppjamtampMissionItemDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("ownerName")
    val ownerName: String? = null,
    @SerialName("level")
    val level: Int,
    @SerialName("profileImage")
    val profileImage: List<String>? = emptyList(),
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
