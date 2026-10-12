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
package org.sopt.official.feature.poke.v2.onboarding.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeRecommendationUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState

/**
 * 콕 찌르기 온보딩 화면 상태.
 *
 * @property isLoading                 추천 프로필 목록 최초 로딩 여부.
 * @property isRefreshing              당겨서 새로고침 진행 여부.
 * @property isError                   목록 조회 실패 여부. `true` 이면 화면 단에서 에러 다이얼로그 → 뒤로가기.
 * @property shouldShowGuideBottomSheet 온보딩을 처음 보는 유저에게 안내 바텀시트를 띄워야 하는지.
 * @property pages                     서버가 내려주는 추천 그룹(`randomInfoList`) 전체를 그룹별 페이지로 변환한 목록.
 *                                      화면에서는 [androidx.compose.foundation.pager.HorizontalPager]로 그룹 수만큼
 *                                      스와이프 가능한 페이지를 그린다.
 * @property pokeTarget                콕 찌르기 메시지 바텀시트에서 선택된 대상. `null` 이면 바텀시트를 숨긴다.
 * @property messages                  [pokeTarget] 에게 보낼 수 있는 메시지 목록.
 * @property isAnonymous               메시지 바텀시트의 익명 체크박스 상태.
 */
@Immutable
data class PokeOnboardingUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,
    val shouldShowGuideBottomSheet: Boolean = false,
    val pages: ImmutableList<PokeRecommendationUiState> = persistentListOf(),
    val pokeTarget: PokeUserUiState? = null,
    val messages: ImmutableList<PokeMessageUiState> = persistentListOf(),
    val isAnonymous: Boolean = true,
)
