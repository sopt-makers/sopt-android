package org.sopt.official.data.model.poke.response

import kotlinx.serialization.SerialName
import org.sopt.official.domain.entity.poke.PokeNotificationHistoryItem

data class PokeNotificationResponse (
    @SerialName("history")
    val history: List<PokeNotificationHistoryItem>,
    @SerialName("pageSize")
    val pageSize: Int,
    @SerialName("pageNum")
    val pageNum: Int
)