package org.sopt.official.feature.appjamtamp.model

import org.sopt.official.domain.appjamtamp.entity.MissionLevel

internal data class Mission(
    val id: Int,
    val title: String,
    val level: MissionLevel,
    val isCompleted: Boolean
)
