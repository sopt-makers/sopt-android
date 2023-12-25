package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.PokeNotificationList

@Serializable
data class PokeNotificationResult(
    @SerialName("history")
    val history: List<PokeUserResult>,
    @SerialName("pageSize")
    val pageSize: Int,
    @SerialName("pageNum")
    val pageNum: Int
) {
    fun toEntity(): PokeNotificationList = PokeNotificationList(
        history = history.map { it.toEntity() },
        pageSize = pageSize,
        pageNum = pageNum,
    )
}