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

import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.BuildConfig
import org.sopt.official.R
import org.sopt.official.auth.PlaygroundAuth
import org.sopt.official.auth.data.PlaygroundAuthDatasource
import org.sopt.official.auth.impl.api.AuthService
import org.sopt.official.auth.impl.model.request.AuthRequest
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.di.Auth
import org.sopt.official.common.util.dp
import org.sopt.official.common.util.setOnAnimationEndListener
import org.sopt.official.common.util.setOnSingleClickListener
import org.sopt.official.common.util.viewBinding
import org.sopt.official.databinding.ActivityAuthBinding
import org.sopt.official.feature.main.MainActivity
import org.sopt.official.network.model.response.OAuthToken
import org.sopt.official.network.persistence.SoptDataStore
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private val viewModel by viewModels<AuthViewModel>()

    @Auth
    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var dataStore: SoptDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (dataStore.accessToken.isNotEmpty()) {
                startActivity(
                    MainActivity.getIntent(this, MainActivity.StartArgs(UserStatus.of(dataStore.userStatus)))
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        setContentView(binding.root)
        initNotificationChannel()

        initUi()
        initAnimation()
        collectUiEvent()
    }

    private fun initNotificationChannel() {
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

    private fun collectUiEvent() {
        viewModel.uiEvent
            .flowWithLifecycle(lifecycle)
            .onEach { event ->
                when (event) {
                    is AuthUiEvent.Success -> startActivity(
                        MainActivity.getIntent(this, MainActivity.StartArgs(event.userStatus))
                    )

                    is AuthUiEvent.Failure -> startActivity(
                        MainActivity.getIntent(this, MainActivity.StartArgs(UserStatus.UNAUTHENTICATED))
                    )
                }
            }.launchIn(lifecycleScope)
    }

    private fun initAnimation() {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in).apply {
            startOffset = 700
        }
        fadeInAnimation.setOnAnimationEndListener {
            binding.groupBottomAuth.isVisible = true
        }
        ObjectAnimator.ofFloat(
            binding.imgSoptLogo,
            "translationY",
            -140.dp.toFloat()
        ).apply {
            duration = 1000
            startDelay = 700
            interpolator = AnimationUtils.loadInterpolator(
                this@AuthActivity,
                android.R.interpolator.fast_out_slow_in
            )
        }.start()
        binding.groupBottomAuth.startAnimation(fadeInAnimation)
    }

    private fun initUi() {
        binding.btnSoptNotMember.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.btnSoptLogin.setOnSingleClickListener {
            PlaygroundAuth.authorizeWithWebTab(
                context = this,
                isDebug = BuildConfig.DEBUG,
                authDataSource = object : PlaygroundAuthDatasource {
                    override suspend fun oauth(code: String): Result<OAuthToken> {
                        return kotlin.runCatching {
                            authService
                                .authenticate(AuthRequest(code, dataStore.pushToken))
                                .toOAuthToken()
                        }
                    }
                }
            ) {
                it.onSuccess { token ->
                    lifecycleScope.launch {
                        viewModel.onLogin(token.toEntity())
                    }
                }.onFailure {
                    lifecycleScope.launch {
                        viewModel.onFailure(it)
                    }
                }
            }
        }
        binding.btnSoptNotMember.setOnSingleClickListener {
            startActivity(
                MainActivity.getIntent(
                    this,
                    MainActivity.StartArgs(
                        UserStatus.UNAUTHENTICATED
                    )
                )
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
