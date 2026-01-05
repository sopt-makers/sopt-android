package org.sopt.official.feature.appjamtamp.missionlist.model

import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity
import org.sopt.official.domain.appjamtamp.entity.MissionLevel

data class AppjamtampMissionUiModel(
    val id: Int = -1,
    val title: String = "",
    val ownerName: String = "",
    val level: MissionLevel = MissionLevel.of(1),
    val profileImage: List<String> = emptyList(),
    val isCompleted: Boolean = false,
)

fun AppjamtampMissionEntity.toUiModel() = AppjamtampMissionUiModel(
    id = id,
    title = title,
    ownerName = ownerName,
    level = MissionLevel.of(level),
    profileImage = profileImage,
    isCompleted = isCompleted,
)
