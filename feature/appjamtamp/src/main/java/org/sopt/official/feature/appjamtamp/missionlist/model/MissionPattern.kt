package org.sopt.official.feature.appjamtamp.missionlist.model

internal class MissionPattern private constructor(
    val length: Float,
    val diameter: Float,
    val gap: Float,
) {
    companion object {
        private const val DIAMETER_RATIO: Float = 0.58f
        private const val GAP_RATIO: Float = 0.21f

        operator fun invoke(length: Float): MissionPattern {
            return MissionPattern(
                length = length,
                diameter = length * DIAMETER_RATIO,
                gap = length * GAP_RATIO,
            )
        }
    }
}
