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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.MainTabRoute
import org.sopt.official.feature.poke.v2.bridge.PokeEntryRoute
import org.sopt.official.feature.poke.v2.bridge.navigation.PokeEntry
import org.sopt.official.feature.poke.v2.component.PokeScaffold
import org.sopt.official.feature.poke.v2.friend.navigation.PokeFriend
import org.sopt.official.feature.poke.v2.main.navigation.PokeMain
import org.sopt.official.feature.poke.v2.onboarding.PokeOnboardingRoute
import org.sopt.official.feature.poke.v2.onboarding.navigation.PokeOnboarding
import org.sopt.official.model.UserStatus

@Serializable
data object PokeGraph : MainTabRoute

fun NavController.navigateToPoke(navOptions: NavOptions? = null) {
    navigate(PokeGraph, navOptions)
}

/**
 * 콕 찌르기 그래프.
 *
 * nav stack 과 controller 는 전부 `feature:main`의 [navController] 가 소유한다 — poke-v2 는
 * 자체 `NavHost`/`rememberNavController()` 를 두지 않고, 전달받은 [navController] 위에
 * 자신의 destination 들만 얹는다(레거시 `feature.poke.navigation.pokeNavGraph` 와 동일한 구조).
 *
 * [PokeGraph] 의 자손인 destination들은 [rememberPokeGraphSnackbarHostState] 를 통해 같은
 * `SnackbarHostState` 를 공유한다(예: [PokeEntry] -> [PokeOnboarding] 로 이동해도 동일 인스턴스).
 * [PokeFriend] 처럼 [PokeGraph] 바깥에 독립적으로 등록된 destination에서 호출하면, 조상에
 * [PokeGraph] 가 없다는 걸 감지해 예외 없이 로컬(비공유) `SnackbarHostState` 로 폴백한다.
 */
fun NavGraphBuilder.pokeGraph(
    navController: NavController,
    userStatus: UserStatus,
    navigateUp: () -> Unit,
    navigateToProfile: (userId: Int) -> Unit,
) {
    navigation<PokeGraph>(startDestination = PokeEntry) {
        // 브릿지 화면 - 로딩. 신규 유저 여부를 판별해 온보딩/메인으로 분기한다.
        composable<PokeEntry> {
            PokeEntryRoute(
                navigateToOnboarding = {
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

        composable<PokeOnboarding> { backStackEntry ->
            val snackbarHostState = rememberPokeGraphSnackbarHostState(navController, backStackEntry)
            val scope = rememberCoroutineScope()

            PokeScaffold(snackbarHostState = snackbarHostState) { innerPadding ->
                PokeOnboardingRoute(
                    navigateUp = navigateUp,
                    onShowSnackbar = { visuals -> scope.launch { snackbarHostState.showSnackbar(visuals) } },
                    navigateToProfile = navigateToProfile,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }

        composable<PokeMain> {
            // TODO PokeMainRoute
            //  현재 상태(확인됨):
            //  - 구현 시 PokeOnboarding과 동일하게 rememberPokeGraphSnackbarHostState(navController, backStackEntry)로
            //    PokeOnboarding과 같은 snackbarHostState를 공유할 것.
        }

        composable<PokeNotification> {
            // TODO PokeNotificationRoute (레거시 이관 예정)
        }
    }

    composable<PokeFriend> {
        // TODO PokeFriendRoute
        //  PokeGraph 밖의 독립 destination이라 rememberPokeGraphSnackbarHostState(navController, backStackEntry)를
        //  써도 PokeGraph를 조상으로 갖지 않으므로 로컬(비공유) SnackbarHostState로 안전하게 폴백한다.
    }
}
