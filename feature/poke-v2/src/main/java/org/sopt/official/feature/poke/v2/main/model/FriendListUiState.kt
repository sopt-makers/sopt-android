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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.domain.poke.type.PokeFriendType

/**
 * 친구 목록 섹션 하나의 UI 상태를 표현하는 데이터 클래스
 *
 * @property friendCount    현재 섹션에 속한 유저 수
 * @property items          섹션에 표시할 [PokeUserUiState] 목록
 */
@Immutable
data class FriendListUiState(
    val friendCount: Int,
    val items: ImmutableList<PokeUserUiState> = persistentListOf()
) {
    val isEmpty: Boolean get() = items.isEmpty()
}

typealias PokeFriendListSections = ImmutableList<Pair<PokeFriendType, FriendListUiState>>

internal fun emptyPokeFriendListSections(): PokeFriendListSections = persistentListOf(
    PokeFriendType.NEW to FriendListUiState(friendCount = 0),
    PokeFriendType.BEST_FRIEND to FriendListUiState(friendCount = 0),
    PokeFriendType.SOULMATE to FriendListUiState(friendCount = 0),
)
