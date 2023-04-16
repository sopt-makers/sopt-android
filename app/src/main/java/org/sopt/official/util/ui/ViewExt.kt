package org.sopt.official.util.ui

import android.view.View

fun View.setVisible(visible: Boolean, isGone: Boolean = true): View = apply {
    visibility = when {
        visible -> View.VISIBLE
        isGone -> View.GONE
        else -> View.INVISIBLE
    }
}