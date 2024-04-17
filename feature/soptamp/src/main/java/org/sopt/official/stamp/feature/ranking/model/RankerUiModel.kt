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
package org.sopt.official.stamp.feature.ranking.model

import org.sopt.official.domain.soptamp.model.Rank

data class RankerUiModel(
    val rank: Int,
    val nickname: String,
    private val description: String? = null,
    val score: Int
) {
    fun getDescription() = description ?: DEFAULT_DESCRIPTION
    fun isTopRank() = (rank <= STANDARD_TOP_RANK)
    fun isNotTopRank() = (rank > STANDARD_TOP_RANK)

    companion object {
        const val DEFAULT_USER_NAME = "-"
        const val STANDARD_TOP_RANK = 3
        const val DEFAULT_DESCRIPTION = "설정된 한 마디가 없습니다"
        val DEFAULT_RANK = RankerUiModel(0, DEFAULT_USER_NAME, null, 0)
    }
}

fun List<Rank>.toUiModel(): RankingListUiModel = RankingListUiModel(
    this.map { it.toUiModel() }
)

fun Rank.toUiModel(): RankerUiModel = RankerUiModel(
    rank = this.rank,
    nickname = this.nickname,
    description = this.profileMessage,
    score = this.point
)
