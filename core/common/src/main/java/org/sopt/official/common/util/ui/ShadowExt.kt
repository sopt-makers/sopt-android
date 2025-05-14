package org.sopt.official.common.util.ui

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp
) = composed {
    val density = LocalDensity.current
    val blurPx = remember(blur) { with(density) { blur.toPx() } }
    val spreadPx = remember(spread) { with(density) { spread.toPx() } }
    val offsetXPx = remember(offsetX) { with(density) { offsetX.toPx() } }
    val offsetYPx = remember(offsetY) { with(density) { offsetY.toPx() } }

    val maskFilter = remember(blurPx) {
        if (blurPx > 0) BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL) else null
    }

    val paint = remember(color, maskFilter) {
        Paint().apply {
            this.color = color
            if (maskFilter != null) {
                this.asFrameworkPaint().maskFilter = maskFilter
            }
        }
    }

    drawBehind {
        val shadowWidth = size.width + spreadPx
        val shadowHeight = size.height + spreadPx
        if (shadowWidth <= 0 || shadowHeight <= 0) return@drawBehind

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(offsetXPx, offsetYPx)
            canvas.drawOutline(shadowOutline, paint)
            canvas.restore()
        }
    }
}
