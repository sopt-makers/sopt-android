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
package org.sopt.official.feature.poke.v2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.sopt.official.feature.poke.v2.bridge.PokeEntryRoute
import org.sopt.official.feature.poke.v2.bridge.navigation.PokeEntry
import org.sopt.official.feature.poke.v2.component.PokeScaffold
import org.sopt.official.feature.poke.v2.component.PokeSnackBarVisuals
import org.sopt.official.feature.poke.v2.friend.navigation.PokeFriend
import org.sopt.official.feature.poke.v2.main.navigation.PokeMain
import org.sopt.official.feature.poke.v2.onboarding.navigation.PokeOnboarding
import org.sopt.official.model.UserStatus

@Serializable
data object PokeGraph

fun NavController.navigateToPoke(navOptions: NavOptions? = null) {
    navigate(PokeGraph, navOptions)
}

fun NavGraphBuilder.pokeGraph(
    userStatus: UserStatus,
    navigateUp: () -> Unit,
) {
    composable<PokeGraph> {
        PokeNavHost(userStatus = userStatus, navigateUp = navigateUp)
    }
}

@Composable
private fun PokeNavHost(
    userStatus: UserStatus,
    navigateUp: () -> Unit,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val onShowSnackbar: (PokeSnackBarVisuals) -> Unit = { visuals ->
        scope.launch { snackbarHostState.showSnackbar(visuals) }
    }

    PokeScaffold(snackbarHostState = snackbarHostState) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PokeEntry,
            modifier = Modifier.padding(innerPadding),
        ) {
            // 브릿지 화면 - 로딩. 신규 유저 여부를 판별해 온보딩/메인으로 분기한다.
            composable<PokeEntry> {
                PokeEntryRoute(
                    navigateToOnboarding = {
                        // TODO(poke-v2): currentGeneration 소스 결정 전까지 기본값 사용
                        navController.navigate(PokeOnboarding(userStatus = userStatus.name))
                    },
                    navigateToMain = {
                        navController.navigate(PokeMain) {
                            popUpTo<PokeEntry> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    navigateUp = navigateUp,
                )
            }
            composable<PokeOnboarding> {
                // TODO PokeOnboardingRoute
            }
            composable<PokeMain> {
                // TODO PokeMainRoute
            }
            composable<PokeFriend> {
                // TODO PokeFriendRoute
            }
            composable<PokeNotification> {
                // TODO PokeNotificationRoute (레거시 이관 예정)
            }
        }
    }
}
