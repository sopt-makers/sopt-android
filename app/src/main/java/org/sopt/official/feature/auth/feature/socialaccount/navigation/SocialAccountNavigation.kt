package org.sopt.official.feature.auth.feature.socialaccount.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.feature.auth.feature.socialaccount.SocialAccountRoute

fun NavController.navigateSocialAccount(
    navOptions: NavOptions? = null
) {
    navigate(
        route = SocialAccount,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.socialAccountNavGraph(
    onGoogleLoginCLick: () -> Unit
) {
    composable<SocialAccount>(
    ) {
        SocialAccountRoute(
            onGoogleLoginCLick = onGoogleLoginCLick
        )
    }
}

@Serializable
data object SocialAccount