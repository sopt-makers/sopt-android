package org.sopt.official.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.core.navigation.MainTabRoute
import org.sopt.official.feature.home.HomeRoute
import org.sopt.official.feature.home.model.HomeEvent

@Serializable
data object Home : MainTabRoute

fun NavController.navigateToHome(
    navOptions: NavOptions? = null,
) {
    navigate(Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    homeEvent: HomeEvent,
    userStatus: UserStatus,
    paddingValues: PaddingValues,
) {
    composable<Home> {
        HomeRoute(
            userStatus = userStatus,
            paddingValues = paddingValues,
            homeEvent = homeEvent,
        )
    }
}
