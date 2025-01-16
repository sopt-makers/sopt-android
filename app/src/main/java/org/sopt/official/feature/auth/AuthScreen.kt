package org.sopt.official.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.auth.component.CertificationSnackBar
import org.sopt.official.feature.auth.feature.authmain.navigation.AuthMainNavigation
import org.sopt.official.feature.auth.feature.authmain.navigation.authMainNavGraph
import org.sopt.official.feature.auth.feature.authmain.navigation.navigateAuthMain
import org.sopt.official.feature.auth.feature.certificate.navigation.certificationNavGraph
import org.sopt.official.feature.auth.feature.certificate.navigation.navigateCertification
import org.sopt.official.feature.auth.feature.socialaccount.navigation.navigateSocialAccount
import org.sopt.official.feature.auth.feature.socialaccount.navigation.socialAccountNavGraph

@Composable
internal fun AuthScreen(
    navigateToUnAuthenticatedHome: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    onContactChannelClick: () -> Unit,
    onGoogleFormClick: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val onShowSnackBar: () -> Unit = remember {
        {
            coroutineScope.launch {
                snackBarHostState.showSnackbar("인증번호가 전송되었어요.")
            }
        }
    }

    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                        .align(alignment = Alignment.TopCenter),
                    snackbar = { message ->
                        CertificationSnackBar(message = message.visuals.message)
                    }
                )
            }
        },
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
                    startDestination = AuthMainNavigation(platform = "") // TODO: platform 값 로컬에서 가져오기
                ) {
                    authMainNavGraph(
                        navigateToUnAuthenticatedHome = navigateToUnAuthenticatedHome,
                        onGoogleLoginCLick = onGoogleLoginCLick,
                        navigateToCertification = { status ->
                            navController.navigateCertification(status)
                        },
                        onContactChannelClick = onContactChannelClick
                    )
                    certificationNavGraph(
                        onBackClick = navController::navigateUp,
                        onShowSnackBar = onShowSnackBar,
                        navigateToSocialAccount = { status, name ->
                            navController.navigateSocialAccount(status, name)
                        },
                        navigateToAuthMain = { platform ->
                            navController.navigateAuthMain(platform)
                        },
                        onGoogleFormClick = onGoogleFormClick
                    )
                    socialAccountNavGraph(
                        onGoogleLoginCLick = {}
                    )
                }
            }
        }
    )
}