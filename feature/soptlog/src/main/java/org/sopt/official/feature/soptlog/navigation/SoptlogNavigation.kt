package org.sopt.official.feature.soptlog.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.MainTabRoute
import org.sopt.official.feature.soptlog.SoptlogRoute

fun NavController.navigateToSoptlog(navOptions: NavOptions) {
    navigate(SoptLog, navOptions)
}

fun NavGraphBuilder.soptlogNavGraph(
    paddingValues: PaddingValues,
) {
    composable<SoptLog> {
        SoptlogRoute(
            paddingValues = paddingValues
        )
    }
}

@Serializable
data object SoptLog : MainTabRoute
