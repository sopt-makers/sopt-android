package org.sopt.official.feature.auth.feature.authmain.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.feature.auth.feature.authmain.AuthMainRoute
import org.sopt.official.feature.auth.model.AuthStatus

fun NavGraphBuilder.authMainNavGraph(
    navigateToUnAuthenticatedHome: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    navigateToCertification: (AuthStatus) -> Unit,
    onContactChannelClick: () -> Unit
) {
    composable<AuthMainNavigation> {
        AuthMainRoute(
            navigateToUnAuthenticatedHome = navigateToUnAuthenticatedHome,
            onGoogleLoginCLick = onGoogleLoginCLick,
            navigateToCertification = navigateToCertification,
            onContactChannelClick = onContactChannelClick
        )
    }
}

@Serializable
data object AuthMainNavigation