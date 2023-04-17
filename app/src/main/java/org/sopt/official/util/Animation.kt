package org.sopt.official.util

import android.view.animation.Animation

inline fun Animation.setOnAnimationEndListener(
    crossinline onAnimationEnd: (animation: Animation?) -> Unit
) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?) {
            onAnimationEnd(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) = Unit
    })
}
