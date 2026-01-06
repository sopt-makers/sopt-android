package org.sopt.official.feature.appjamtamp.missionlist.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionListEntity
import org.sopt.official.domain.appjamtamp.entity.MissionLevel

data class AppjamtampMissionListUiModel(
    val teamNumber: String = "",
    val teamName: String = "",
    val missionList: ImmutableList<AppjamtampMissionUiModel> = persistentListOf(),
)

internal fun AppjamtampMissionListEntity.toUiModel() = AppjamtampMissionListUiModel(
    teamNumber = teamNumber,
    teamName = teamName,
    missionList = missions.map { it.toUiModel() }.toImmutableList()
)

data class AppjamtampMissionUiModel(
    val id: Int = -1,
    val title: String = "",
    val ownerName: String? = "",
    val level: MissionLevel = MissionLevel.of(1),
    val profileImage: List<String>? = emptyList(),
    val isCompleted: Boolean = false,
)

internal fun AppjamtampMissionEntity.toUiModel() = AppjamtampMissionUiModel(
    id = id,
    title = title,
    ownerName = ownerName,
    level = MissionLevel.of(level),
    profileImage = profileImage,
    isCompleted = isCompleted,
)
