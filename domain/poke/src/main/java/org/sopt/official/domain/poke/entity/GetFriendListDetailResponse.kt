package org.sopt.official.domain.poke.entity

class GetFriendListDetailResponse : BaseResponse<FriendListDetail>()

data class FriendListDetail(
    val friendList: List<PokeUser>,
    val pageSize: Int,
    val pageNum: Int,
)