package org.sopt.official.common.util

import android.os.SystemClock
import androidx.compose.ui.Modifier
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
