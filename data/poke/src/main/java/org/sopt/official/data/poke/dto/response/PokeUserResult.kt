/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.PokeUser

@Serializable
data class PokeUserResult(
    @SerialName("userId")
    val userId: Int,
    @SerialName("playgroundId")
    val playgroundId: Int,
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
    @SerialName("mutualRelationMessage")
    val mutualRelationMessage: String,
    @SerialName("isFirstMeet")
    val isFirstMeet: Boolean,
    @SerialName("isAlreadyPoke")
    val isAlreadyPoke: Boolean,
    @SerialName("isAnonymous")
    val isAnonymous: Boolean,
    @SerialName("anonymousName")
    val anonymousName: String,
    @SerialName("anonymousImage")
    val anonymousImage: String,
) {
    fun toEntity(): PokeUser = PokeUser(
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
        anonymousImage = anonymousImage
    )
}
