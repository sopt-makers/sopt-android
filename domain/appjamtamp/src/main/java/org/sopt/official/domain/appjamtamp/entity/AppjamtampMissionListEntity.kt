package org.sopt.official.domain.appjamtamp.entity

data class AppjamtampMissionListEntity(
    val teamNumber: String,
    val teamName: String,
    val missions: List<AppjamtampMissionEntity>
)
