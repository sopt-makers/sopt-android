package org.sopt.official.stamp.domain.model

import org.sopt.official.stamp.domain.MissionLevel
import org.sopt.official.stamp.feature.mission.model.MissionUiModel

data class OtherMission(
    val nickname: String,
    val profileMessage: String,
    val userMissions: List<Mission>
) {
    data class Mission(
        val id: Int,
        val title: String,
        val level: Int,
        val display: Boolean,
        val profileImage: String? = null,
    ) {
        fun toUiModel(): MissionUiModel = MissionUiModel(
            id = this.id,
            title = this.title,
            level = MissionLevel.of(this.level),
            isCompleted = this.display,
        )
    }
}
