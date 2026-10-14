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
package org.sopt.official.feature.poke.v2.bridge.model

import androidx.compose.runtime.Immutable

/**
 * 브릿지(로딩) 화면 상태.
 *
 * @property isLoading   `checkNewInPoke` 호출 진행 중 여부.
 * @property isError     조회 실패 여부. `true` 이면 화면 단에서 뒤로가기 처리.
 * @property isNewPoke   신규 유저 여부. `null` 이면 아직 판별 전. `true` → 온보딩, `false` → 메인.
 */
@Immutable
data class PokeEntryUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val isNewPoke: Boolean? = null,
) {
    val destination: PokeEntryDestination
        get() = when {
            isError -> PokeEntryDestination.Back
            isLoading || isNewPoke == null -> PokeEntryDestination.Loading
            isNewPoke -> PokeEntryDestination.Onboarding
            else -> PokeEntryDestination.Main
        }
}

/** 브릿지 화면이 판별을 마친 뒤 이동할 목적지. */
enum class PokeEntryDestination {
    Loading,
    Onboarding,
    Main,
    Back,
}
