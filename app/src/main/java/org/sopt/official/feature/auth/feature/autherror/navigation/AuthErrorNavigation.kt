package org.sopt.official.feature.auth.feature.autherror.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.feature.auth.feature.autherror.AuthErrorRoute
import org.sopt.official.feature.auth.model.AuthStatus

internal fun NavController.navigateAuthError(
    navOptions: NavOptions? = null,
) {
    navigate(
        route = AuthErrorNavigation,
        navOptions = navOptions,
    )
}

internal fun NavGraphBuilder.authErrorNavGraph(
    onRetryClick: () -> Unit,
    navigateToCertification: (AuthStatus) -> Unit,
) {
    composable<AuthErrorNavigation> {
        AuthErrorRoute(
            onRetryClick = onRetryClick,
            navigateToCertification = navigateToCertification,
        )
    }
}

@Serializable
internal data object AuthErrorNavigation
