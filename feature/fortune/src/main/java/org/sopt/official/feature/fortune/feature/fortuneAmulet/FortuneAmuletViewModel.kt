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
package org.sopt.official.feature.fortune.feature.fortuneAmulet

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.fortune.usecase.GetTodayFortuneCardUseCase

typealias GraphicColor = android.graphics.Color

@Inject
@ViewModelKey(FortuneAmuletViewModel::class)
@ContributesIntoMap(AppScope::class)
internal class FortuneAmuletViewModel @Inject constructor(
    getTodayFortuneCardUseCase: GetTodayFortuneCardUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<FortuneAmuletState> = MutableStateFlow(FortuneAmuletState())
    val state: StateFlow<FortuneAmuletState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                _state.value = _state.value.copy(isLoading = true)
                getTodayFortuneCardUseCase()
            }.onSuccess { todayFortuneCard ->
                _state.update {
                    it.copy(
                        description = todayFortuneCard.description,
                        imageColor = parseColor(todayFortuneCard.imageColorCode),
                        imageUrl = todayFortuneCard.imageUrl,
                        name = todayFortuneCard.name,
                        isLoading = false
                    )
                }
            }.onFailure {
                _state.value = _state.value.copy(isFailure = true)
            }
        }
    }

    private fun parseColor(colorCode: String): Color = try {
        Color(GraphicColor.parseColor(colorCode))
    } catch (e: IllegalArgumentException) {
        Color.White
    }
}