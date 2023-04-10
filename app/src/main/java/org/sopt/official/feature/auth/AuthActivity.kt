package org.sopt.official.feature.auth

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.R
import org.sopt.official.databinding.ActivityAuthBinding
import org.sopt.official.util.dp
import org.sopt.official.util.viewBinding
import timber.log.Timber

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in_fade_in)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in).apply {
            startOffset = 700
        }
        zoomInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) = Unit

            override fun onAnimationEnd(animation: Animation?) {
                ObjectAnimator.ofFloat(binding.imgSoptLogo, "translationY", -140.dp.toFloat()).apply {
                    duration = 1000
                    interpolator = AnimationUtils.loadInterpolator(this@AuthActivity, android.R.interpolator.fast_out_slow_in)
                }.start()
                binding.groupBottomAuth.startAnimation(fadeInAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
        })
        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) = Unit

            override fun onAnimationEnd(animation: Animation?) {
                binding.groupBottomAuth.isVisible = true
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
        })
        binding.imgSoptLogo.startAnimation(zoomInAnimation)
    }
}
