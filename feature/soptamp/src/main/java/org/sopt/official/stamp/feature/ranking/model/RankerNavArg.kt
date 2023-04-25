/*
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.ranking.model

import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer

data class RankerNavArg(
    val nickname: String,
    val description: String
)

fun RankerUiModel.toArgs() = RankerNavArg(
    nickname = this.nickname,
    description = this.getDescription()
)

@NavTypeSerializer
class RankerNavArgsSerializer : DestinationsNavTypeSerializer<RankerNavArg> {
    override fun fromRouteString(routeStr: String): RankerNavArg {
        val (nickname, description) = routeStr.split("::")
        return RankerNavArg(
            nickname = nickname,
            description = description
        )
    }

    override fun toRouteString(value: RankerNavArg): String {
        return "${value.nickname}::${value.description}"
    }
}
