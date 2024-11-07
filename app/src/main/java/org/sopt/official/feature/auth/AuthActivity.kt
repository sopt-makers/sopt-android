/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.official.BuildConfig
import org.sopt.official.R
import org.sopt.official.auth.PlaygroundAuth
import org.sopt.official.auth.data.PlaygroundAuthDatasource
import org.sopt.official.auth.impl.api.AuthService
import org.sopt.official.auth.impl.model.request.AuthRequest
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.di.Auth
import org.sopt.official.common.util.setOnAnimationEndListener
import org.sopt.official.common.util.setOnSingleClickListener
import org.sopt.official.common.util.viewBinding
import org.sopt.official.databinding.ActivityAuthBinding
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.AuthTextWithArrow
import org.sopt.official.feature.home.HomeActivity
import org.sopt.official.network.model.response.OAuthToken
import org.sopt.official.network.persistence.SoptDataStore
import javax.inject.Inject

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
        setContent {
            SoptTheme {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current

                LaunchedEffect(true) {
                    if (dataStore.accessToken.isNotEmpty()) {
                        startActivity(
                            HomeActivity.getIntent(context, HomeActivity.StartArgs(UserStatus.of(dataStore.userStatus)))
                        )
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
                                    HomeActivity.getIntent(context, HomeActivity.StartArgs(event.userStatus))
                                )

                                is AuthUiEvent.Failure -> startActivity(
                                    HomeActivity.getIntent(context, HomeActivity.StartArgs(UserStatus.UNAUTHENTICATED))
                                )
                            }
                        }
                }

                AuthScreen()
            }
        }
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
            //  -140.dp.toFloat()
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
                HomeActivity.getIntent(
                    this,
                    HomeActivity.StartArgs(
                        UserStatus.UNAUTHENTICATED
                    )
                )
            )
        }
    }

    @Composable
    fun AuthScreen() {
        var showAuthBottom by remember { mutableStateOf(false) }
        val offsetY = remember { Animatable(0f) }

        LaunchedEffect(showAuthBottom) {
            offsetY.animateTo(
                targetValue = -140f,
                animationSpec = tween(durationMillis = 700)
            )
            showAuthBottom = true
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.img_logo),
                    contentDescription = "솝트 로고",
                    modifier = Modifier.offset(y = offsetY.value.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            AnimatedVisibility(
                visible = showAuthBottom,
                enter = fadeIn(initialAlpha = 0.3f),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                AuthBottom()
            }
        }
    }

    @Composable
    private fun AuthBottom() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AuthButton(
                paddingVertical = 12.dp,
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_auth_google),
                    contentDescription = "구글 로고",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "Google로 로그인",
                    style = SoptTheme.typography.label16SB
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextWithArrow(text = "로그인이 안 되나요?")
            Spacer(modifier = Modifier.height(44.dp))
            AuthDivider()
            Spacer(modifier = Modifier.height(16.dp))
            AuthButton(
                paddingVertical = 12.dp,
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                containerColor = Gray700,
                contentColor = White
            ) {
                Text(
                    text = "SOPT 회원가입",
                    style = SoptTheme.typography.label16SB
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextWithArrow(text = "나중에 로그인할래요.")
            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    @Composable
    private fun AuthDivider() {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(
                thickness = 0.6.dp,
                color = Gray300,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "또는",
                color = Gray300,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(
                thickness = 0.6.dp,
                color = Gray300,
                modifier = Modifier.weight(1f)
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun AuthScreenPreview() {
        SoptTheme {
            AuthScreen()
        }
    }
}
