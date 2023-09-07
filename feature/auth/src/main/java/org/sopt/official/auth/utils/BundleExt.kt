package org.sopt.official.playground.auth.utils

import android.os.Build
import android.os.Bundle

internal inline fun <reified T> Bundle.getParcelableAs(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(key) as? T
    }
}