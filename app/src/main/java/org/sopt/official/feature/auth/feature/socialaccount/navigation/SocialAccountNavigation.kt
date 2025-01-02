package org.sopt.official.feature.auth.feature.socialaccount.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.sopt.official.feature.auth.feature.socialaccount.SocialAccountRoute
import org.sopt.official.feature.auth.model.AuthStatus

fun NavController.navigateSocialAccount(
    status: AuthStatus,
    navOptions: NavOptions? = null
) {
    navigate(
        route = SocialAccount(status = status),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.socialAccountNavGraph(
    onGoogleLoginCLick: () -> Unit
) {
    composable<SocialAccount> {
        val args = it.toRoute<SocialAccount>()
        SocialAccountRoute(
            status = args.status,
            onGoogleLoginCLick = onGoogleLoginCLick
        )
    }
}

@Serializable
data class SocialAccount(
    val status: AuthStatus
)