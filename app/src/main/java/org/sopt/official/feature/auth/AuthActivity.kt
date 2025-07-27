/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.R
import org.sopt.official.auth.impl.api.AuthService
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.di.Auth
import org.sopt.official.common.view.toast
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.main.MainActivity
import org.sopt.official.feature.mypage.web.WebUrlConstant
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val viewModel by viewModels<AuthViewModel>()

    @Auth
    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var dataStore: SoptDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                val scope = rememberCoroutineScope()

                LaunchedEffect(true) {
                    try {
                        if (dataStore.accessToken.isNotEmpty()) {
                            startActivity(
                                MainActivity.getIntent(context, MainActivity.StartArgs(UserStatus.of(dataStore.userStatus)))
                            )
                        }
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }

                LaunchedEffect(true) {
                    NotificationChannel(
                        getString(R.string.toolbar_notification),
                        getString(R.string.toolbar_notification),
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        setSound(null, null)
                        enableLights(false)
                        enableVibration(false)
                        lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(this)
                    }
                }

                LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
                    viewModel.uiEvent.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
                        .collect { event ->
                            when (event) {
                                is AuthUiEvent.Success -> startActivity(
                                    MainActivity.getIntent(context, MainActivity.StartArgs(event.userStatus))
                                )

                                is AuthUiEvent.Failure -> startActivity(
                                    MainActivity.getIntent(context, MainActivity.StartArgs(UserStatus.UNAUTHENTICATED))
                                )
                            }
                        }
                }

                LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
                    viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
                        .collect { sideEffect ->
                            when (sideEffect) {
                                is AuthSideEffect.ShowToast -> context.toast(sideEffect.message)
                            }
                        }
                }

                AuthScreen(
                    navigateToUnAuthenticatedHome = {
                        startActivity(
                            MainActivity.getIntent(
                                this,
                                MainActivity.StartArgs(
                                    UserStatus.UNAUTHENTICATED
                                )
                            )
                        )
                    },
                    onContactChannelClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.OPINION_KAKAO_CHAT))) },
                    onGoogleFormClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.SOPT_GOOGLE_FROM))) },
                    platform = dataStore.platform
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
