package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.FriendListDetail

@Serializable
data class GetFriendListDetailResult(
    @SerialName("friendList")
    val friendList: List<PokeUserResult>,
    @SerialName("pageSize")
    val pageSize: Int,
    @SerialName("pageNum")
    val pageNum: Int,
) {
    fun toEntity(): FriendListDetail = FriendListDetail(
        friendList = friendList.map { it.toEntity() },
        pageSize = pageSize,
        pageNum = pageNum,
    )
}