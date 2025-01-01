package org.sopt.official.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.auth.feature.authmain.navigation.AuthMainNavigation
import org.sopt.official.feature.auth.feature.authmain.navigation.authMainNavGraph
import org.sopt.official.feature.auth.feature.certificate.navigation.certificationNavGraph
import org.sopt.official.feature.auth.feature.certificate.navigation.navigateCertification

@Composable
internal fun AuthScreen(
    navigateToUnAuthenticatedHome: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValue ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SoptTheme.colors.background)
                    .padding(paddingValue)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = AuthMainNavigation
                ) {
                    authMainNavGraph(
                        navigateToUnAuthenticatedHome = {
                            navigateToUnAuthenticatedHome()
                        },
                        onGoogleLoginCLick = {
                            onGoogleLoginCLick()
                        },
                        navigateToCertification = {
                            navController.navigateCertification()
                        }
                    )
                    certificationNavGraph()
                }
            }
        }
    )
}