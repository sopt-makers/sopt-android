package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.FriendListSummary

@Serializable
data class GetFriendListSummaryResult(
    @SerialName("newFriend")
    val newFriend: List<PokeUserResult>,
    @SerialName("newFriendSize")
    val newFriendSize: Int,
    @SerialName("bestFriend")
    val bestFriend: List<PokeUserResult>,
    @SerialName("bestFriendSize")
    val bestFriendSize: Int,
    @SerialName("soulmate")
    val soulmate: List<PokeUserResult>,
    @SerialName("soulmateSize")
    val soulmateSize: Int,
) {
    fun toEntity(): FriendListSummary = FriendListSummary(
        newFriend = newFriend.map { it.toEntity() },
        newFriendSize = newFriendSize,
        bestFriend = bestFriend.map { it.toEntity() },
        bestFriendSize = bestFriendSize,
        soulmate = soulmate.map { it.toEntity() },
        soulmateSize = soulmateSize,
    )
}