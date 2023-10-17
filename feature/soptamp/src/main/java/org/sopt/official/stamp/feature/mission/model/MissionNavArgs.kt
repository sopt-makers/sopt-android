/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.mission.model

import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer
import org.sopt.official.domain.soptamp.MissionLevel

data class MissionNavArgs(
    val id: Int,
    val title: String,
    val level: MissionLevel,
    val isCompleted: Boolean,
    val isMe: Boolean,
    val nickname: String
)

fun MissionUiModel.toArgs(isMe: Boolean, nickname: String) = MissionNavArgs(
    id = this.id,
    title = this.title,
    level = this.level,
    isCompleted = this.isCompleted,
    isMe = isMe,
    nickname = nickname
)

@NavTypeSerializer
class MissionNavArgsSerializer : DestinationsNavTypeSerializer<MissionNavArgs> {
    override fun toRouteString(value: MissionNavArgs): String {
        return "${value.id}::${value.title}::${value.level}::${value.isCompleted}::${value.isMe}::${value.nickname}"
    }

    override fun fromRouteString(routeStr: String): MissionNavArgs {
        val it = routeStr.split("::")
        return MissionNavArgs(
            id = it[0].toInt(),
            title = it[1],
            level = MissionLevel.of(it[2].toInt()),
            isCompleted = it[3].toBoolean(),
            isMe = it[4].toBoolean(),
            nickname = it[5],
        )
    }
}
