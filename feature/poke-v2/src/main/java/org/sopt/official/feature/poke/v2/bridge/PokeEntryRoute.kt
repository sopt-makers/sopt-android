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
package org.sopt.official.feature.poke.v2.bridge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.indicator.LoadingIndicator
import org.sopt.official.feature.poke.v2.bridge.model.PokeEntryDestination

/**
 * 브릿지(로딩) 라우트.
 *
 * UI 는 없고, [PokeEntryViewModel] 이 판별한 [PokeEntryDestination] 에 따라
 * 온보딩 / 메인으로 라우팅하거나 뒤로가기만 한다.
 * 실제 로딩 인디케이터 등 시각 요소는 상위 Scaffold 또는 각 목적지 화면에서 담당한다.
 */
@Composable
fun PokeEntryRoute(
    navigateToOnboarding: () -> Unit,
    navigateToMain: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: PokeEntryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.destination) {
        when (uiState.destination) {
            PokeEntryDestination.Onboarding -> navigateToOnboarding()
            PokeEntryDestination.Main -> navigateToMain()
            PokeEntryDestination.Back -> navigateUp()
            PokeEntryDestination.Loading -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        if (uiState.destination == PokeEntryDestination.Loading) {
            LoadingIndicator()
        }
    }
}
