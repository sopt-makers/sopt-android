/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.auth.component.CertificationSnackBar
import org.sopt.official.feature.auth.feature.authmain.navigation.AuthMainNavigation
import org.sopt.official.feature.auth.feature.authmain.navigation.authMainNavGraph
import org.sopt.official.feature.auth.feature.authmain.navigation.navigateAuthMain
import org.sopt.official.feature.auth.feature.certificate.navigation.CertificationNavigation
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
    platform: String,
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
                    startDestination = AuthMainNavigation(platform = platform)
                ) {
                    authMainNavGraph(
                        navigateToUnAuthenticatedHome = navigateToUnAuthenticatedHome,
                        onGoogleLoginCLick = onGoogleLoginCLick,
                        navigateToCertification = { status ->
                            navController.navigateCertification(
                                status = status
                            )
                        },
                        onContactChannelClick = onContactChannelClick
                    )
                    certificationNavGraph(
                        onBackClick = navController::navigateUp,
                        onShowSnackBar = onShowSnackBar,
                        navigateToSocialAccount = { status, name ->
                            navController.navigateSocialAccount(
                                status = status,
                                name = name,
                                navOptions = NavOptions.Builder().setPopUpTo(
                                    route = CertificationNavigation(status),
                                    inclusive = true
                                ).build()
                            )
                        },
                        navigateToAuthMain = { platform ->
                            val navOptions = navOptions {
                                popUpTo(id = navController.graph.id) {
                                    inclusive = true
                                }
                            }
                            navController.navigateAuthMain(
                                platform = platform,
                                navOptions = navOptions
                            )
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
