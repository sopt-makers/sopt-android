package org.sopt.official.feature.auth.feature.authmain.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.sopt.official.feature.auth.feature.authmain.AuthMainRoute
import org.sopt.official.feature.auth.model.AuthStatus

fun NavController.navigateAuthMain(
    platform: String,
    navOptions: NavOptions? = null
) {
    navigate(
        route = AuthMainNavigation(platform = platform),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.authMainNavGraph(
    navigateToUnAuthenticatedHome: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    navigateToCertification: (AuthStatus) -> Unit,
    onContactChannelClick: () -> Unit
) {
    composable<AuthMainNavigation> {
        val args = it.toRoute<AuthMainNavigation>()
        AuthMainRoute(
            platform = args.platform,
            navigateToUnAuthenticatedHome = navigateToUnAuthenticatedHome,
            onGoogleLoginCLick = onGoogleLoginCLick,
            navigateToCertification = navigateToCertification,
            onContactChannelClick = onContactChannelClick
        )
    }
}

@Serializable
data class AuthMainNavigation(
    val platform: String
)