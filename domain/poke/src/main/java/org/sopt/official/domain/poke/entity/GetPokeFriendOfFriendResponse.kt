package org.sopt.official.domain.poke.entity

class GetPokeFriendOfFriendListResponse : BaseResponse<List<PokeFriendOfFriendList>>()

data class PokeFriendOfFriendList(
    val friendId: Long,
    val playgroundId: Int,
    val friendName: String,
    val friendProfileImage: String,
    val friendList: List<PokeUser>,
)
