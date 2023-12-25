package org.sopt.official.domain.poke.entity

class GetPokeNotificationListResponse : BaseResponse<PokeNotificationList>()

data class PokeNotificationList(
    val history: List<PokeUser>,
    val pageSize: Int,
    val pageNum: Int,
)
