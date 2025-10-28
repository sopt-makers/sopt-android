package org.sopt.official.stamp.feature.mission.detail.model

import org.sopt.official.domain.soptamp.model.StampClap

data class StampClapUiModel(
    val clapCount: Int,
) {
    fun toDomain(): StampClap {
        return StampClap(
            clapCount = clapCount,
        )
    }
}
