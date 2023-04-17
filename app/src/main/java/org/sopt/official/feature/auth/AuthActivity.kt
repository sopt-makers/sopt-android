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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.BuildConfig
import org.sopt.official.MainActivity
import org.sopt.official.R
import org.sopt.official.databinding.ActivityAuthBinding
import org.sopt.official.playground.auth.PlaygroundAuth
import org.sopt.official.util.dp
import org.sopt.official.util.setOnAnimationEndListener
import org.sopt.official.util.toEntity
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private val viewModel by viewModels<AuthViewModel>()

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
            .onEach {
                when (it) {
                    is AuthUiEvent.Success -> {
                        startActivity(MainActivity.getIntent(this))
                    }

                    is AuthUiEvent.Failure -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
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
            interpolator = AnimationUtils.loadInterpolator(this@AuthActivity, android.R.interpolator.fast_out_slow_in)
        }.start()
        binding.groupBottomAuth.startAnimation(fadeInAnimation)
    }

    private fun initUi() {
        binding.btnSoptNotMember.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.btnSoptLogin.setOnClickListener {
            PlaygroundAuth.authorizeWithWebTab(this, isDebug = BuildConfig.DEBUG) {
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
            startActivity(MainActivity.getIntent(this))
        }
    }
}
