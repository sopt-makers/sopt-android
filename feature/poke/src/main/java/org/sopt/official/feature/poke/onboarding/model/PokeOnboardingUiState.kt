/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.poke.onboarding.model

import java.io.Serializable
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser

data class PokeOnboardingUiState(
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
        val isAnonymous: Boolean,
        val anonymousName: String,
        val anonymousImage: String,
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
            isAnonymous = isAnonymous,
            anonymousName = anonymousName,
            anonymousImage = anonymousImage,
        )
    }
}

fun PokeRandomUserList.PokeRandomUsers.toSerializable() = PokeOnboardingUiState(
    randomType = randomType,
    randomTitle = randomTitle,
    userInfoList = userInfoList.map { it.toSerializable() },
)

fun PokeUser.toSerializable() = PokeOnboardingUiState.PokeUsers(
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
    isAnonymous = isAnonymous,
    anonymousName = anonymousName,
    anonymousImage = anonymousImage,
)
