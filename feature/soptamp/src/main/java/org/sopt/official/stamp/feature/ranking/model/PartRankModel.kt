package org.sopt.official.stamp.feature.ranking.model

import org.sopt.official.domain.soptamp.model.PartRank

data class PartRankModel(
    val rank: Int,
    val part: String,
    val point: Int
)

internal fun List<PartRank>.toData(): List<PartRankModel> = this.map {
    PartRankModel(
        rank = it.rank,
        part = it.part,
        point = it.points
    )
}
