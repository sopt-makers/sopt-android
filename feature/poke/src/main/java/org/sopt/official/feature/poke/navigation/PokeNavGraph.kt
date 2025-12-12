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
package org.sopt.official.feature.poke.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sopt.official.feature.poke.bridge.PokeEntryRoute
import org.sopt.official.feature.poke.friend.summary.FriendListSummaryScreen
import org.sopt.official.feature.poke.main.PokeScreen
import org.sopt.official.feature.poke.notification.PokeNotificationScreen
import org.sopt.official.feature.poke.onboarding.OnboardingScreen
import org.sopt.official.model.UserStatus

fun NavGraphBuilder.pokeNavGraph(
    navController: NavController,
    userStatus: UserStatus,
    paddingValues: PaddingValues,
) {
    navigation<PokeGraph> (
        startDestination = PokeEntry
    ) {
        // 브릿지 화면 - 로딩
        composable<PokeEntry> {
            PokeEntryRoute(
                paddingValues = paddingValues,
                navController = navController,
                userStatus = userStatus
            )
        }

        // 콕 찌르기 메인 화면
        composable<PokeMain> {
            PokeScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
                navigateToPokeNotification = {
                    navController.navigate(PokeNotification(userStatus.name))
                }
            )
        }

        // 온보딩 화면
        composable<PokeOnboarding> {
            OnboardingScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
                navigateToPokeMain = {
                    navController.navigate(PokeMain) {
                        popUpTo<PokeOnboarding> {
                            inclusive = true
                        }
                    }
                },
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }

        // 알림 화면
        composable<PokeNotification> {
            PokeNotificationScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
            )
        }

        // 친한 친구 리스트
        composable<PokeFriendList> {
            FriendListSummaryScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
            )
        }
    }
}
