package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.PokeFriend

@Serializable
data class PokeFriendResult(
    @SerialName("userId")
    val userId: Long,
    @SerialName("playgroundId")
    val playgroundId: Long,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("name")
    val name: String,
    @SerialName("message")
    val message: String,
    @SerialName("generation")
    val generation: Int,
    @SerialName("part")
    val part: String,
    @SerialName("pokeNum")
    val pokeNum: Int,
    @SerialName("relationName")
    val relationName: String,
    @SerialName("mutual")
    val mutual: List<String>,
    @SerialName("isFirstMeet")
    val isFirstMeet: Boolean,
    @SerialName("isAlreadyPoke")
    val isAlreadyPoke: Boolean
) {
    fun toEntity(): PokeFriend = PokeFriend(
        userId = userId,
        profileImage = profileImage,
        name = name,
        generation = generation,
        part = part,
        pokeNum = pokeNum,
        isAlreadyPoke = isAlreadyPoke
    )
}