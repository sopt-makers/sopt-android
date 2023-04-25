package org.sopt.official.util

import android.content.res.Resources

val Int.dp
    get() = this * Resources.getSystem().displayMetrics.density.toInt()

val Int.px
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
