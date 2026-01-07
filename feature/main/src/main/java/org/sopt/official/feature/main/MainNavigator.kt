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
package org.sopt.official.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import org.sopt.official.core.navigation.Route
import org.sopt.official.feature.appjamtamp.navigation.navigateToAppjamtamp
import org.sopt.official.feature.home.navigation.Home
import org.sopt.official.feature.home.navigation.navigateToHome
import org.sopt.official.feature.poke.navigation.PokeGraph
import org.sopt.official.feature.poke.navigation.navigateToPokeEntry
import org.sopt.official.feature.soptlog.navigation.navigateToSoptLog
import org.sopt.official.model.UserStatus
import org.sopt.official.stamp.feature.navigation.navigateToSoptamp

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    private val currentDestinationCheck: NavDestination?
        get() = navController.currentDestination

    fun isSameTab(tab: MainTab): Boolean {
        return currentDestinationCheck?.hierarchy?.any { it.hasRoute(tab::class) } == true
    }

    val startDestination = Home

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hierarchy?.any { it.hasRoute(tab::class) } == true
        }

    fun navigate(tab: MainTab, userStatus: UserStatus, onFailure: () -> Unit = {}) {
        val navOptions = navOptions {
            navController.currentDestination?.route?.let {
                popUpTo(it) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        if (userStatus == UserStatus.UNAUTHENTICATED) {
            onFailure()
            return
        }

        when (tab) {
            MainTab.Home -> navController.navigateToHome(
                navOptions = navOptions
            )

            MainTab.Soptamp -> navController.navigateToSoptamp(
                navOptions = navOptions
            )

            MainTab.Appjamtamp -> navController.navigateToAppjamtamp(
                navOptions = navOptions
            )

            MainTab.Poke -> {
                navController.navigateToPokeEntry(
                    navOptions = navOptions
                )
            }

            MainTab.SoptLog -> {
                navController.navigateToSoptLog(
                    navOptions = navOptions
                )
            }
        }
    }

    fun navigateAndClear(
        tab: MainTab,
        userStatus: UserStatus,
        onFailure: () -> Unit = {}
    ) {
        when (tab) {
            MainTab.Home -> navController.navigateToHome(
                navOptions = navOptions {
                    popUpTo<Home> { inclusive = true }
                    launchSingleTop = true
                }
            )

            MainTab.Soptamp -> {
                navController.navigateToSoptamp(
                    navOptions = navOptions {
                        popUpTo<Home> { inclusive = true }
                        launchSingleTop = true
                    }
                )
            }

            MainTab.Appjamtamp -> {
                navController.navigateToAppjamtamp(
                    navOptions = navOptions {
                        popUpTo<Home> { inclusive = true }
                        launchSingleTop = true
                    }
                )
            }

            MainTab.Poke -> { // PokeGraph가 있다고 가정
                navController.navigateToPokeEntry(
                    navOptions = navOptions {
                        popUpTo<PokeGraph> { inclusive = true }
                        launchSingleTop = true
                    }
                )
            }

            MainTab.SoptLog -> {
                when (userStatus) {
                    UserStatus.ACTIVE, UserStatus.INACTIVE -> {
                        navController.navigateToSoptLog(
                            navOptions = navOptions {
                                launchSingleTop = true
                            }
                        )
                    }

                    UserStatus.UNAUTHENTICATED -> onFailure()
                }
            }
        }
    }

    private fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateUpIfNotStartDestination() {
        if (!isSameCurrentDestination<Home>()) {
            navigateUp()
        }
    }

    private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }

    @Composable
    fun shouldShowBottomBar() = MainTab.contains { tab ->
        currentDestination?.hierarchy?.any { it.hasRoute(tab::class) } == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
