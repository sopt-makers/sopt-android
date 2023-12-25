package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.PokeFriendOfFriendList

@Serializable
data class PokeFriendOfFriendListResult(
    @SerialName("friendId")
    val friendId: Long,
    @SerialName("friendName")
    val friendName: String,
    @SerialName("friendProfileImage")
    val friendProfileImage: String,
    @SerialName("friendList")
    val friendList: List<FriendOfFriendResult>
) {
    fun toEntity(): PokeFriendOfFriendList = PokeFriendOfFriendList(
        friendId = friendId,
        friendName = friendName,
        friendProfileImage = friendProfileImage,
        friendList = friendList.map { it.toEntity() }
    )

    @Serializable
    data class FriendOfFriendResult(
        @SerialName("userId")
        val userId: Long,
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
        val isAlreadyPoke: Boolean,
    ) {
        fun toEntity(): PokeFriendOfFriendList.PokeFriendOfFriend = PokeFriendOfFriendList.PokeFriendOfFriend(
            userId = userId,
            name = name,
            profileImage = profileImage,
            generation = generation,
            part = part,
            message = message,
            relationName = relationName,
            mutual = mutual,
            isFirstMeet = isFirstMeet,
            isAlreadyPoke = isAlreadyPoke,
        )
    }
}
