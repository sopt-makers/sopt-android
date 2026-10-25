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
package org.sopt.official.feature.poke.v2.friend.model

import androidx.compose.runtime.Immutable
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState

/**
 * 콕찌르기 이후 관계 변화 시 보여주는 로띠 진행 단계
 *
 * - 단짝친구: BestFriend
 * - 천생연분: Soulmate -> 로띠 끝나면 SoulmateRevealed(정체 공개)
 *
 * @property user   찌른 대상 친구
 */

sealed interface PokeRelationChangeState {
    val user: PokeUserUiState

    @Immutable
    data class BestFriend(
        override val user: PokeUserUiState
    ) : PokeRelationChangeState

    @Immutable
    data class Soulmate(
        override val user: PokeUserUiState
    ) : PokeRelationChangeState

    @Immutable
    data class SoulmateRevealed(
        override val user: PokeUserUiState
    ) : PokeRelationChangeState
}
