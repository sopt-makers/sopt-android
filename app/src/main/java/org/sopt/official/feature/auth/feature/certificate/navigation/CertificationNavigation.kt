package org.sopt.official.feature.auth.feature.certificate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.sopt.official.feature.auth.feature.certificate.CertificationRoute
import org.sopt.official.feature.auth.model.AuthStatus

fun NavController.navigateCertification(
    status: AuthStatus,
    navOptions: NavOptions? = null
) {
    navigate(
        route = CertificationNavigation(status = status),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.certificationNavGraph(
    onBackClick: () -> Unit,
    onShowSnackBar: () -> Unit,
    navigateToSocialAccount: () -> Unit
) {
    composable<CertificationNavigation> {
        val args = it.toRoute<CertificationNavigation>()
        CertificationRoute(
            status = args.status,
            onBackClick = onBackClick,
            onShowSnackBar = onShowSnackBar,
            navigateToSocialAccount = navigateToSocialAccount
        )
    }
}

@Serializable
data class CertificationNavigation(
    val status: AuthStatus
)