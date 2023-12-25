package org.sopt.official.data.model.poke.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeFriendOfFriendResponse(
    @SerialName("friendId")
    val friendId: Long,
    @SerialName("playgroundId")
    val playgroundId: Long,
    @SerialName("friendName")
    val friendName: String,
    @SerialName("friendProfileImage")
    val friendProfileImage: String,
    @SerialName("friendList")
    val friendList: List<FriendOfFriend>
) {
    @Serializable
    data class FriendOfFriend(
        @SerialName("userId")
        val userId: Long,
        @SerialName("playgroundId")
        val playgroundId: Long,
        @SerialName("name")
        val name: String,
        @SerialName("profileImage")
        val profileImage: String,
        @SerialName("generation")
        val generation: Int,
        @SerialName("part")
        val part: String,
        @SerialName("message")
        val message: String,
        @SerialName("relationName")
        val relationName: String,
        @SerialName("mutual")
        val mutual: List<String>,
        @SerialName("isFirstMeet")
        val isFirstMeet: Boolean,
        @SerialName("isAlreadyPoke")
        val isAlreadyPoke: Boolean
    )
}
