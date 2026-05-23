/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.common.util.getVersionName
import org.sopt.official.common.util.launchPlayStore
import org.sopt.official.config.FcmPushTokenManager
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.auth.component.UpdateDialog
import org.sopt.official.feature.main.MainActivity
import org.sopt.official.feature.mypage.web.WebUrlConstant
import org.sopt.official.localstorage.source.TokenStorage
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.UserStatus
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val viewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var userStorage: UserStorage
    @Inject
    lateinit var tokenStorage: TokenStorage

    @Inject
    lateinit var fcmPushTokenManager: FcmPushTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoptTheme {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current

                val accessToken by tokenStorage.accessToken.collectAsStateWithLifecycle(initialValue = "")
                val platform by userStorage.platform.collectAsStateWithLifecycle(initialValue = "")
                val updateState by viewModel.updateState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.getUpdateConfig(context.getVersionName())
                }

                when (val state = updateState) {
                    is UpdateState.Default -> {}
                    is UpdateState.PatchUpdateAvailable -> {
                        var dialogVisibility by rememberSaveable { mutableStateOf(true) }

                        if (dialogVisibility) {
                            UpdateDialog(
                                description = state.message,
                                onDismiss = {
                                    dialogVisibility = false
                                    navigateToMainActivity(accessToken)
                                },
                                onPositiveClick = this@AuthActivity::launchPlayStore,
                                onNegativeClick = {
                                    dialogVisibility = false
                                    navigateToMainActivity(accessToken)
                                }
                            )
                        }
                    }

                    is UpdateState.UpdateRequired -> {
                        UpdateDialog(
                            description = state.message,
                            onDismiss = this@AuthActivity::finishAffinity,
                            onPositiveClick = this@AuthActivity::launchPlayStore,
                            onNegativeClick = this@AuthActivity::finishAffinity,
                        )
                    }

                    else -> navigateToMainActivity(accessToken)
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

                AuthScreen(
                    navigateToHome = {
                        try {
                            if (accessToken.isNotEmpty()) {
                                startActivity(
                                    MainActivity.getIntent(
                                        context = context,
                                        args = MainActivity.StartArgs(UserStatus.ACTIVE)
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                    },
                    navigateToUnAuthenticatedHome = {
                        startActivity(
                            MainActivity.getIntent(
                                context = this,
                                args = MainActivity.StartArgs(UserStatus.UNAUTHENTICATED)
                            )
                        )
                    },
                    onContactChannelClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.OPINION_KAKAO_CHAT))) },
                    onGoogleFormClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.SOPT_GOOGLE_FROM))) },
                    platform = platform
                )
            }
        }
    }

    private fun navigateToMainActivity(accessToken : String) {
        try {
            if (accessToken.isNotEmpty()) {
                lifecycleScope.launch {
                    runCatching {
                        fcmPushTokenManager.registerCurrentToken()
                    }.onFailure {
                        Timber.e(it, "자동 로그인 후 FCM 토큰 서버 등록 실패")
                    }
                }

                startActivity(
                    MainActivity.getIntent(
                        context = this,
                        args = MainActivity.StartArgs(UserStatus.ACTIVE)
                    )
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
