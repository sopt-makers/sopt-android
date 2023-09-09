/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.domain

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

        fun of(level: Int): MissionLevel {
            validateMissionLevel(level)
            return MissionLevel(level)
        }

        private fun validateMissionLevel(level: Int) {
            require(level in MINIMUM_LEVEL..MAXIMUM_LEVEL) {
                "Mission Level 은 $MINIMUM_LEVEL ~ $MAXIMUM_LEVEL 사이 값이어야 합니다."
            }
        }
    }
}
