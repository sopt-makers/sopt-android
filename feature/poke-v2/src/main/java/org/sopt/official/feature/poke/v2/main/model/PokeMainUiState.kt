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

/**
 * 콕 찌르기 메인 화면 상태.
 *
 * @property isLoading      최초 로딩 여부(세 API 중 하나라도 아직 로딩 중).
 * @property isRefreshing   당겨서 새로고침 진행 여부.
 * @property pokeMe         나를 찌른 사람 카드. 없으면 `null`.
 * @property friend         내 친구 카드. 없으면 `null`.
 * @property recommendations 추천 프로필 섹션 목록.
 */
@Immutable
data class PokeMainUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val pokeMe: PokeUserUiState? = null,
    val friend: PokeUserUiState? = null,
    val recommendations: ImmutableList<PokeRecommendationUiState> = persistentListOf(),
) {
    val hasPokeMe: Boolean get() = pokeMe != null
    val hasFriend: Boolean get() = friend != null
}
