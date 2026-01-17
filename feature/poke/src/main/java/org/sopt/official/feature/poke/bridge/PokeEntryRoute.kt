/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.bridge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.sopt.official.feature.poke.main.PokeScreen
import org.sopt.official.feature.poke.navigation.navigateToPokeNotification
import org.sopt.official.feature.poke.onboarding.OnboardingScreen
import org.sopt.official.model.UserStatus

@Composable
fun PokeEntryRoute(
    paddingValues: PaddingValues,
    navController: NavController,
    userStatus: UserStatus,
    viewModel: PokeEntryViewModel = metroViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.isError) {
        if (uiState.isError) {
            navController.popBackStack()
        }
    }
    when {
        uiState.isLoading || uiState.isNewPoke == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(width = 32.dp),
                    color = colorScheme.secondary,
                    trackColor = colorScheme.surfaceVariant,
                    strokeWidth = 4.dp,
                )
            }
        }

        // 신규 유저 -> 온보딩 화면
        uiState.isNewPoke == true -> {
            OnboardingScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
                navigateUp = navController::popBackStack,
                navigateToPokeMain = viewModel::updateToOldUser,
            )
        }

        // 기존 유저(혹은 온보딩 완료 후) -> 콕 찌르기 메인 화면
        else -> {
            PokeScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
                navigateToPokeNotification = {
                    navController.navigateToPokeNotification(userStatus.name)
                },
            )
        }
    }
}
