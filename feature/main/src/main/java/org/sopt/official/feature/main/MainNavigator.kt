package org.sopt.official.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.core.navigation.Route
import org.sopt.official.feature.home.navigation.Home
import org.sopt.official.feature.home.navigation.navigateToHome
import org.sopt.official.feature.soptlog.navigation.navigateToSoptlog

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Home

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }


    fun navigate(tab: MainTab, userStatus: UserStatus, onFailure: () -> Unit = {}) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainTab.Home -> navController.navigateToHome(
                navOptions = navOptions
            )

            MainTab.SoptLog -> {
                when (userStatus) {
                    UserStatus.ACTIVE, UserStatus.INACTIVE -> {
                        navController.navigateToSoptlog(
                            navOptions = navOptions
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
    fun shouldShowBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
