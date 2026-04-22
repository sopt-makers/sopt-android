/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.common.util

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.unit.IntSize

/**
 * 연속 클릭을 방지하기 위해 [throttleTime] 동안 클릭 이벤트를 무시하며,
 * 리플 효과 없이 클릭 이벤트를 처리하는 Modifier
 *
 * @param onClick 클릭 이벤트 콜백
 * @param throttleTime 클릭 이벤트 간 최소 간격 (기본값 1000ms)
 */
fun Modifier.throttledNoRippleClickable(
    onClick: () -> Unit,
    throttleTime: Long = 1000L,
): Modifier = this.then(
    ThrottledNoRippleClickableElement(throttleTime, onClick)
)

private data class ThrottledNoRippleClickableElement(
    private val throttleTime: Long,
    private val onClick: () -> Unit,
) : ModifierNodeElement<ThrottledNoRippleClickableNode>() {
    override fun create() = ThrottledNoRippleClickableNode(throttleTime, onClick)
    override fun update(node: ThrottledNoRippleClickableNode) {
        node.onClick = onClick
    }
}

private class ThrottledNoRippleClickableNode(
    private val throttleTime: Long,
    var onClick: () -> Unit,
) : PointerInputModifierNode, Modifier.Node() {

    private var lastClickTime = 0L

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize,
    ) {
        if (pass == PointerEventPass.Main &&
            pointerEvent.type == PointerEventType.Release
        ) {
            val nowTime = SystemClock.elapsedRealtime()
            if (nowTime - lastClickTime >= throttleTime) {
                lastClickTime = nowTime
                onClick()
            }
        }
    }

    override fun onCancelPointerInput() = Unit
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier =
    composed {
        then(
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onClick()
            },
        )
    }
