package org.sopt.official.feature.poke.onboarding.model

import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser
import java.io.Serializable

data class PokeRandomUsers(
    val randomType: String,
    val randomTitle: String,
    val userInfoList: List<PokeUsers>,
) : Serializable {
    data class PokeUsers(
        val userId: Int,
        val playgroundId: Int,
        val profileImage: String,
        val name: String,
        val message: String,
        val generation: Int,
        val part: String,
        val pokeNum: Int,
        val relationName: String,
        val mutualRelationMessage: String,
        val isFirstMeet: Boolean,
        var isAlreadyPoke: Boolean,
    ) : Serializable {
        fun toEntity() = PokeUser(
            userId = userId,
            playgroundId = playgroundId,
            profileImage = profileImage,
            name = name,
            message = message,
            generation = generation,
            part = part,
            pokeNum = pokeNum,
            relationName = relationName,
            mutualRelationMessage = mutualRelationMessage,
            isFirstMeet = isFirstMeet,
            isAlreadyPoke = isAlreadyPoke,
        )
    }
}

fun PokeRandomUserList.PokeRandomUsers.toSerializable() = PokeRandomUsers(
    randomType = randomType,
    randomTitle = randomTitle,
    userInfoList = userInfoList.map { it.toSerializable() },
)

fun PokeUser.toSerializable() = PokeRandomUsers.PokeUsers(
    userId = userId,
    playgroundId = playgroundId,
    profileImage = profileImage,
    name = name,
    message = message,
    generation = generation,
    part = part,
    pokeNum = pokeNum,
    relationName = relationName,
    mutualRelationMessage = mutualRelationMessage,
    isFirstMeet = isFirstMeet,
    isAlreadyPoke = isAlreadyPoke,
)
