package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.PokeUser

@Serializable
data class PokeUserResult(
    @SerialName("userId")
    val userId: Int,
    @SerialName("playgroundId")
    val playGroundId: Int,
    @SerialName("profileImage")
    val profileImage: String?,
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
    val mutual: List<PokeUserResult>,
    @SerialName("isFirstMeet")
    val isFirstMeet: Boolean,
    @SerialName("isAlreadyPoke")
    val isAlreadyPoke: Boolean,
) {
    fun toEntity(): PokeUser = PokeUser(
        userId = userId,
        playGroundId = playGroundId,
        profileImage = profileImage,
        name = name,
        message = message,
        generation = generation,
        part = part,
        pokeNum = pokeNum,
        relationName = relationName,
        mutual = mutual.map { it.toEntity() },
        isFirstMeet = isFirstMeet,
        isAlreadyPoke = isAlreadyPoke,
    )
}