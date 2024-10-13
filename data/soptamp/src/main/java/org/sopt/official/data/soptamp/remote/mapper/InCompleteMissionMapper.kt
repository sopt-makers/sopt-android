package org.sopt.official.data.soptamp.remote.mapper

import org.sopt.official.data.soptamp.remote.model.MissionData
import org.sopt.official.data.soptamp.remote.model.response.InCompleteMissionResponse

internal fun InCompleteMissionResponse.toData(): MissionData = MissionData(
    id = this.id,
    title = this.title,
    level = this.level,
    profileImage = this.profileImage,
    isCompleted = false
)

internal fun List<InCompleteMissionResponse>.toData(): List<MissionData> = this.map { it.toData() }
