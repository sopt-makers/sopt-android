package org.sopt.official.feature.auth

import android.animation.ObjectAnimator
import android.graphics.Paint
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import org.sopt.official.auth.data.remote.model.response.OAuthToken
import org.sopt.official.data.model.request.AuthRequest
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.service.AuthService
import org.sopt.official.databinding.ActivityAuthBinding
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.feature.main.MainActivity
import org.sopt.official.util.dp
import org.sopt.official.util.setOnAnimationEndListener
import org.sopt.official.util.setOnSingleClickListener
import org.sopt.official.util.toEntity
import org.sopt.official.util.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private val viewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var dataStore: SoptDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initUi()
        initAnimation()
        collectUiEvent()
    }

    private fun collectUiEvent() {
        viewModel.uiEvent
            .flowWithLifecycle(lifecycle)
            .onEach { event ->
                when (event) {
                    is AuthUiEvent.Success -> {
                        val args = MainActivity.StartArgs(event.userStatus)
                        startActivity(MainActivity.getIntent(this, args))
                    }

                    is AuthUiEvent.Failure -> {
                        val args = MainActivity.StartArgs(UserStatus.UNAUTHENTICATED)
                        startActivity(MainActivity.getIntent(this, args))
                    }
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
        binding.btnSoptNotMember.setOnClickListener {
            startActivity(
                MainActivity.getIntent(
                    this,
                    MainActivity.StartArgs(UserStatus.UNAUTHENTICATED)
                )
            )
        }
    }
}
