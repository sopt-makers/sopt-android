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

import org.sopt.official.stamp.domain.model.Rank

data class RankerUiModel(
    val rank: Int,
    val userId: Int,
    val nickname: String,
    private val description: String? = null,
    val score: Int
) {
    fun getDescription() = description ?: DEFAULT_DESCRIPTION
    fun isTopRank() = (rank <= STANDARD_TOP_RANK)
    fun isNotTopRank() = (rank > STANDARD_TOP_RANK)

    companion object {
        private const val DEFAULT_USER_NAME = "-"
        const val STANDARD_TOP_RANK = 3
        const val DEFAULT_DESCRIPTION = "설정된 한 마디가 없습니다"
        val DEFAULT_RANK = RankerUiModel(0, 1, DEFAULT_USER_NAME, null, 0)
    }
}

fun List<Rank>.toUiModel(): RankingListUiModel = RankingListUiModel(
    this.map { it.toUiModel() }
)

fun Rank.toUiModel(): RankerUiModel = RankerUiModel(
    rank = this.rank,
    userId = this.userId,
    nickname = this.nickname,
    description = this.profileMessage,
    score = this.point
)
