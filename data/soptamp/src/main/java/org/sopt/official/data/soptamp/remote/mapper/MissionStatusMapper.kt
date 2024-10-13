package org.sopt.official.data.soptamp.remote.mapper

import org.sopt.official.data.soptamp.remote.model.MissionData
import org.sopt.official.data.soptamp.remote.model.response.MissionStatusResponse

internal fun List<MissionStatusResponse>.toData(isCompleted: Boolean = false): List<MissionData> = this.map { it.toData(isCompleted) }

internal fun MissionStatusResponse.toData(isCompleted: Boolean = false): MissionData = MissionData(
    id = this.id,
    title = this.title,
    level = this.level,
    profileImage = this.profileImage,
    isCompleted = isCompleted
)
