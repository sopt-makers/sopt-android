/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
