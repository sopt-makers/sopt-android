package org.sopt.official.feature.auth

import android.animation.ObjectAnimator
import android.graphics.Paint
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.R
import org.sopt.official.databinding.ActivityAuthBinding
import org.sopt.official.util.dp
import org.sopt.official.util.setOnAnimationEndListener
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initUi()
        initAnimation()
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
    }
}
