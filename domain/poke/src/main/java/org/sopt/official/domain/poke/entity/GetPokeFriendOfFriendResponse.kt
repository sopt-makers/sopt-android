package org.sopt.official.domain.poke.entity

class GetPokeFriendOfFriendListResponse : BaseResponse<List<PokeFriendOfFriendList>>()

data class PokeFriendOfFriendList(
    val friendId: Long,
    val friendName: String,
    val friendProfileImage: String,
    val friendList: List<PokeFriendOfFriend>,
) {
    data class PokeFriendOfFriend(
        val userId: Long,
        val name: String,
        val profileImage: String,
        val generation: Int,
        val part: String,
        val message: String,
        val relationName: String,
        val mutual: List<String>,
        val isFirstMeet: Boolean,
        val isAlreadyPoke: Boolean,
    )
}

