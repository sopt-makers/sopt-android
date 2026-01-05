package org.sopt.official.domain.appjamtamp.entity

data class AppjamtampMissionEntity(
    val id: Int,
    val title: String,
    val ownerName: String?,
    val level: Int,
    val profileImage: List<String>?,
    val isCompleted: Boolean,
)
