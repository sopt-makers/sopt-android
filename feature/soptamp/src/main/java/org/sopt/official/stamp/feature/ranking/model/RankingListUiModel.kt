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

class RankingListUiModel private constructor(
    val topRankingList: Triple<RankerUiModel, RankerUiModel, RankerUiModel>,
    val otherRankingList: List<RankerUiModel>
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
                rankingList.getOtherRanking()
            )
        }

        private fun List<RankerUiModel>.getTopRanking(): Triple<RankerUiModel, RankerUiModel, RankerUiModel> {
            val topRankPartition = this.partition { it.isTopRank() }.first.toMutableList()
            if (topRankPartition.size != RankerUiModel.STANDARD_TOP_RANK) {
                repeat(RankerUiModel.STANDARD_TOP_RANK - topRankPartition.size) {
                    topRankPartition.add(RankerUiModel.DEFAULT_RANK)
                }
            }
            return Triple(
                topRankPartition[0],
                topRankPartition[1],
                topRankPartition[2]
            )
        }

        private fun List<RankerUiModel>.getOtherRanking(): List<RankerUiModel> {
            return this.filter { it.isNotTopRank() }
        }
    }
}
