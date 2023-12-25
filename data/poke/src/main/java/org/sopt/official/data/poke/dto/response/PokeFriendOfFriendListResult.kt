package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.PokeFriendOfFriendList

@Serializable
data class PokeFriendOfFriendListResult(
    @SerialName("friendId")
    val friendId: Long,
    @SerialName("playgroundId")
    val playgroundId: Int,
    @SerialName("friendName")
    val friendName: String,
    @SerialName("friendProfileImage")
    val friendProfileImage: String,
    @SerialName("friendList")
    val friendList: List<PokeUserResult>
) {
    fun toEntity(): PokeFriendOfFriendList = PokeFriendOfFriendList(
        friendId = friendId,
        playgroundId = playgroundId,
        friendName = friendName,
        friendProfileImage = friendProfileImage,
        friendList = friendList.map { it.toEntity() }
    )
}
