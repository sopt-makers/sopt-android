package org.sopt.official.domain.appjamtamp.entity

class MissionLevel private constructor(
    val value: Int
) {
    override fun toString(): String {
        return "$value"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MissionLevel

        return value == other.value
    }

    override fun hashCode(): Int {
        return value
    }

    companion object {
        const val MINIMUM_LEVEL = 1
        const val MAXIMUM_LEVEL = 3
        const val SPECIAL_LEVEL = 10

        fun of(level: Int): MissionLevel {
            validateMissionLevel(level)
            return MissionLevel(level)
        }

        private fun validateMissionLevel(level: Int) {
            require(level in MINIMUM_LEVEL..SPECIAL_LEVEL) {
                "Mission Level 은 $MINIMUM_LEVEL ~ $SPECIAL_LEVEL 사이 값이어야 합니다."
            }
        }
    }
}
