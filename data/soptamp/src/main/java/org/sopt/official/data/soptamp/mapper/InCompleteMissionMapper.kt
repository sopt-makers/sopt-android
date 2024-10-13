package org.sopt.official.data.soptamp.mapper

import org.sopt.official.data.soptamp.remote.model.response.InCompleteMissionResponse
import org.sopt.official.domain.soptamp.model.Mission

internal fun List<InCompleteMissionResponse>.toDomain(): List<Mission> = this.map { it.toDomain() }

internal fun InCompleteMissionResponse.toDomain(): Mission = Mission(
    id = this.id,
    title = this.title,
    level = this.level,
    profileImage = this.profileImage,
    isCompleted = false
)
