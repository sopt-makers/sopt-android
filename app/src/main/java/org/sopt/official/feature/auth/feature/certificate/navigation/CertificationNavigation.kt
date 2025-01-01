package org.sopt.official.feature.auth.feature.certificate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.feature.auth.feature.certificate.CertificationRoute

fun NavController.navigateCertification(
    navOptions: NavOptions? = null
) {
    navigate(
        route = CertificationNavigation,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.certificationNavGraph(
) {
    composable<CertificationNavigation> {
        CertificationRoute()
    }
}

@Serializable
data object CertificationNavigation