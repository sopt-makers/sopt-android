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

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.MainTabRoute
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.feature.poke.v2.bridge.PokeEntryRoute
import org.sopt.official.feature.poke.v2.bridge.navigation.PokeEntry
import org.sopt.official.feature.poke.v2.component.PokeScaffold
import org.sopt.official.feature.poke.v2.friend.navigation.PokeFriend
import org.sopt.official.feature.poke.v2.main.navigation.PokeMain
import org.sopt.official.feature.poke.v2.onboarding.PokeOnboardingRoute
import org.sopt.official.feature.poke.v2.onboarding.navigation.PokeOnboarding
import org.sopt.official.model.UserStatus
import org.sopt.official.webview.view.WebViewActivity

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
 */
fun NavGraphBuilder.pokeGraph(
    navController: NavController,
    userStatus: UserStatus,
    navigateUp: () -> Unit,
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

        composable<PokeOnboarding> {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            PokeScaffold(snackbarHostState = snackbarHostState) { innerPadding ->
                PokeOnboardingRoute(
                    navigateUp = navigateUp,
                    onShowSnackbar = { visuals -> scope.launch { snackbarHostState.showSnackbar(visuals) } },
                    navigateToProfile = { userId ->
                        Intent(context, WebViewActivity::class.java).apply {
                            putExtra(WebViewActivity.INTENT_URL, context.getString(R.string.poke_user_profile_url, userId))
                            context.startActivity(this)
                        }
                    },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }

        composable<PokeMain> {
            // TODO PokeMainRoute
        }

        composable<PokeNotification> {
            // TODO PokeNotificationRoute (레거시 이관 예정)
        }
    }

    composable<PokeFriend> {
        // TODO PokeFriendRoute
    }
}
