package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.PartRank

@Serializable
data class PartRankResponse(
    @SerialName("part")
    val part: String,
    @SerialName("rank")
    val rank: Int,
    @SerialName("points")
    val points: Int,
) {
    fun toDomain() = PartRank(
        part = part,
        rank = rank,
        points = points
    )
}
