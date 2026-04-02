package org.sopt.official.common.util

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * 물결 효과가 있는 중복 클릭 방지를 위한 clickable
 * @param delayMillis 클릭 무시 시간 (기본값 3000ms)
 */
inline fun Modifier.singleClickable(
    delayMillis: Long = 3000L,
    crossinline onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.clickable {
        val currentTime = SystemClock.uptimeMillis()
        if (currentTime - lastClickTime >= delayMillis) {
            lastClickTime = currentTime
            onClick()
        }
    }
}

/**
 * 물결 효과가 없는 중복 클릭 방지를 위한 clickable
 * @param delayMillis 클릭 무시 시간 (기본값 300ms)
 */
inline fun Modifier.singleNoRippleClickable(
    delayMillis: Long = 300L,
    crossinline onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        val currentTime = SystemClock.uptimeMillis()
        if (currentTime - lastClickTime >= delayMillis) {
            lastClickTime = currentTime
            onClick()
        }
    }
}