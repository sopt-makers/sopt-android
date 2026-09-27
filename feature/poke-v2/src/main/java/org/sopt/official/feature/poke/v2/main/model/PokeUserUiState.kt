/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.v2.main.model

import androidx.compose.runtime.Immutable
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType

/**
 * 찌르기 대상 유저 한 명의 UI 상태
 *
 * @property userId                 유저 고유 식별 ID
 * @property userName               프로필 이름
 * @property anonymousName          익명 상태일 때 표시되는 이름
 * @property userGeneration         SOPT 기수
 * @property userPart               SOPT 파트
 * @property profileImageUrl        프로필 이미지 URL
 * @property pokeCount              누적 찌르기 횟수
 * @property relationName           친구 관계명
 * @property isAnonymous            익명 여부
 * @property isPokeButtonEnabled    찌르기 버튼 활성화 여부
 * @property isFirstMeet            처음 찌르는 상대 여부
 */
@Immutable
data class PokeUserUiState(
    val userId: Int,
    val userName: String,
    val anonymousName: String = "",
    val userGeneration: Int,
    val userPart: String,
    val profileImageUrl: String?,
    val pokeCount: Int = 0,
    val relationName: String = "",
    val isAnonymous: Boolean = false,
    val isPokeButtonEnabled: Boolean = true,
    val isFirstMeet: Boolean = false,
) {
    val displayName: String
        get() = if (isAnonymousVisible && anonymousName.isNotBlank()) anonymousName else userName

    val generationPartText: String
        get() = "${userGeneration}기 $userPart"

    val isAnonymousVisible: Boolean
        get() = isAnonymous && !(relationName == PokeFriendType.SOULMATE.readableName && pokeCount == 11)

    val isBestFriend: Boolean
        get() = pokeCount in BEST_FRIEND_RANGE && isAnonymous

    val isSoulMate: Boolean
        get() = pokeCount in SOUL_MATE_RANGE

    val isAnonymousCheckboxLocked: Boolean
        get() = pokeCount >= SOUL_MATE_MIN_POKE_COUNT

    companion object {
        private val BEST_FRIEND_RANGE = 5..6
        private val SOUL_MATE_RANGE = 11..12
        private const val SOUL_MATE_MIN_POKE_COUNT = 10
    }
}

fun PokeUser.toPokeUserUiState() = PokeUserUiState(
    userId = userId,
    userName = name,
    anonymousName = anonymousName,
    userGeneration = generation,
    userPart = part,
    profileImageUrl = profileImage,
    pokeCount = pokeNum,
    relationName = relationName,
    isAnonymous = isAnonymous,
    isPokeButtonEnabled = !isAlreadyPoke,
    isFirstMeet = isFirstMeet,
)
