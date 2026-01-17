/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
import dev.zacsweers.metro.Inject
class RankingListUiModel private constructor(
    val topRankingList: RankersUiModel,
    val otherRankingList: List<RankerUiModel>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RankingListUiModel

        if (topRankingList != other.topRankingList) return false
        if (otherRankingList != other.otherRankingList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = topRankingList.hashCode()
        result = 31 * result + otherRankingList.hashCode()
        return result
    }

    override fun toString(): String {
        return "RankingListUiModel(topRankingList=$topRankingList, otherRankingList=$otherRankingList)"
    }

    companion object {
        operator fun invoke(rankingList: List<RankerUiModel>): RankingListUiModel {
            return RankingListUiModel(
                rankingList.getTopRanking(),
                rankingList.getOtherRanking(),
            )
        }

        private fun List<RankerUiModel>.getTopRanking(): RankersUiModel {
            val topRankPartition = this.partition { it.isTopRank() }.first.toMutableList()
            if (topRankPartition.size != RankerUiModel.STANDARD_TOP_RANK) {
                repeat(RankerUiModel.STANDARD_TOP_RANK - topRankPartition.size) {
                    topRankPartition.add(RankerUiModel.DEFAULT_RANK)
                }
            }
            return RankersUiModel(
                topRankPartition[0],
                topRankPartition[1],
                topRankPartition[2],
            )
        }

        private fun List<RankerUiModel>.getOtherRanking(): List<RankerUiModel> {
            return this.filter { it.isNotTopRank() }
        }
    }
}
