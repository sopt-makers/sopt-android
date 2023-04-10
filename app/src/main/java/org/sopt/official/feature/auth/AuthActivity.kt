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
        val zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in_fade_in)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in).apply {
            startOffset = 700
        }
        zoomInAnimation.setOnAnimationEndListener {
            ObjectAnimator.ofFloat(binding.imgSoptLogo, "translationY", -140.dp.toFloat()).apply {
                duration = 1000
                interpolator = AnimationUtils.loadInterpolator(this@AuthActivity, android.R.interpolator.fast_out_slow_in)
            }.start()
            binding.groupBottomAuth.startAnimation(fadeInAnimation)
        }
        fadeInAnimation.setOnAnimationEndListener {
            binding.groupBottomAuth.isVisible = true
        }
        binding.imgSoptLogo.startAnimation(zoomInAnimation)
    }

    private fun initUi() {
        binding.btnSoptNotMember.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}
