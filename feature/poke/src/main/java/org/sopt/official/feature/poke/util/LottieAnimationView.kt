package org.sopt.official.feature.poke.util

import android.animation.Animator
import com.airbnb.lottie.LottieAnimationView

fun LottieAnimationView.addOnAnimationEndListener(action: () -> Unit) {
    this.addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {
            action()
        }

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationRepeat(animation: Animator) {}
    })
}
