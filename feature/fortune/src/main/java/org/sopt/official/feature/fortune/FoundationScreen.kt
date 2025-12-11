/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.fortune

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.sopt.official.analytics.compose.ProvideTracker
import org.sopt.official.analytics.impl.FakeTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.R.drawable.ic_alert
import org.sopt.official.feature.fortune.R.drawable.ic_poke_check
import org.sopt.official.feature.fortune.component.FortuneTopBar
import org.sopt.official.feature.fortune.feature.fortuneAmulet.navigation.FortuneAmulet
import org.sopt.official.feature.fortune.feature.fortuneAmulet.navigation.fortuneAmuletNavGraph
import org.sopt.official.feature.fortune.feature.fortuneDetail.component.PokeSnackBar
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.SnackBarUiState
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.SnackBarUiState.Anonymous
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.SnackBarUiState.Poke
import org.sopt.official.feature.fortune.feature.fortuneDetail.navigation.FortuneDetail
import org.sopt.official.feature.fortune.feature.fortuneDetail.navigation.fortuneDetailNavGraph
import org.sopt.official.feature.fortune.feature.home.navigation.Home
import org.sopt.official.feature.fortune.feature.home.navigation.homeNavGraph

@Composable
fun FoundationScreen(
    navigateToSoptLog: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var snackBarUiState by remember { mutableStateOf<SnackBarUiState>(Anonymous) }
    val snackBarHostState = remember { SnackbarHostState() }
    val (icon, title) = remember {
        when (snackBarUiState) {
            is Poke -> ic_poke_check to "콕 찌르기를 완료했어요."
            is Anonymous -> ic_alert to "익명 해제 시, 상대방이 나를 알 수 있어요."
        }
    }

    BackHandler { navigateToSoptLog() }

    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(alignment = TopCenter),
                    snackbar = {
                        PokeSnackBar(
                            icon = icon,
                            title = title,
                        )
                    },
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            FortuneTopBar(
                isEnabled = !isBottomSheetVisible,
                onClickNavigationIcon = navigateToSoptLog,
            )
        },
        content = { paddingValue ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SoptTheme.colors.background)
                    .padding(paddingValue)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Home
                ) {
                    homeNavGraph(
                        navigateToFortuneDetail = { date ->
                            navController.navigate(FortuneDetail(date))
                        }
                    )

                    fortuneDetailNavGraph(
                        navigateToFortuneAmulet = {
                            navController.navigate(FortuneAmulet)
                        },
                        isBottomSheetVisible = {
                            isBottomSheetVisible = it
                        },
                        snackBarHostState = snackBarHostState,
                        showSnackBar = { case ->
                            snackBarUiState = case
                        },
                    )

                    fortuneAmuletNavGraph(
                        navigateToSoptLog = navigateToSoptLog
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FoundationScreenPreview() {
    SoptTheme {
        ProvideTracker(FakeTracker) {
            FoundationScreen(
                navigateToSoptLog = {}
            )
        }
    }
}
