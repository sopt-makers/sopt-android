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
import kotlinx.collections.immutable.toPersistentList
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser

/**
 * "친구일 수도 있는 사람" / 온보딩 추천 프로필 섹션 하나의 UI 상태.
 *
 * @property randomType 서버가 내려주는 추천 그룹 타입(`ALL`, 파트명 등). 온보딩 페이징에 사용.
 * @property title      섹션 제목(`randomTitle`).
 * @property users      섹션에 표시할 [PokeUserUiState] 목록.
 */
@Immutable
data class PokeRecommendationUiState(
    val randomType: String,
    val title: String,
    val users: ImmutableList<PokeUserUiState> = persistentListOf(),
) {
    val isEmpty: Boolean get() = users.isEmpty()
}

fun PokeRandomUserList.PokeRandomUsers.toPokeRecommendationUiState() = PokeRecommendationUiState(
    randomType = randomType,
    title = randomTitle,
    users = userInfoList.map(PokeUser::toPokeUserUiState).toPersistentList(),
)

fun PokeRandomUserList.toPokeRecommendationUiStates(): ImmutableList<PokeRecommendationUiState> =
    randomInfoList.map { it.toPokeRecommendationUiState() }.toPersistentList()
