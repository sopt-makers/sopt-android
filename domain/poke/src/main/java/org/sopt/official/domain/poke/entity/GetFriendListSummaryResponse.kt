package org.sopt.official.domain.poke.entity

class GetFriendListSummaryResponse : BaseResponse<FriendListSummary>()

data class FriendListSummary(
    val newFriend: List<PokeUser>,
    val newFriendSize: Int,
    val bestFriend: List<PokeUser>,
    val bestFriendSize: Int,
    val soulmate: List<PokeUser>,
    val soulmateSize: Int,
)