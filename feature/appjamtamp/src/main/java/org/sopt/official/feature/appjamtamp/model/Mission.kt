package org.sopt.official.feature.appjamtamp.model

import org.sopt.official.domain.appjamtamp.entity.MissionLevel

internal data class Mission(
    val id: Int,
    val title: String,
    val level: MissionLevel,
    val isCompleted: Boolean
) {
    companion object {
        val DEFAULT = Mission(
            id = -1,
            title = "",
            level = MissionLevel.of(1),
            isCompleted = false
        )
    }
}
