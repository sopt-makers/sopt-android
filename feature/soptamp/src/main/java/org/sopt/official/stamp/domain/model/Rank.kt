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
package org.sopt.official.stamp.domain.model

data class Rank(
    val rank: Int,
    val nickname: String,
    val point: Int,
    val profileMessage: String?
)

sealed interface RankFetchType {
    data object All : RankFetchType

    /*
     * 당장은 현재 기수만 가져오고 있지만
     * 이후 확장을 할 때 기수 별로 가져올 수 있기 때문에
     * 현재 기수 가져오기를 data class로 기수 값을 넣어서 사용할 수 있게 한다
     */
    data class Term(val value: Int = CURRENT) : RankFetchType

    companion object {
        private const val CURRENT: Int = 33
    }
}
