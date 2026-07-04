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
package org.sopt.official.feature.sopletter.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.feature.sopletter.main.model.imageRes
import org.sopt.official.feature.sopletter.main.model.memoColor

@Composable
internal fun SopletterMemoCard(
    memo: SopletterMessage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {
    Box(
        modifier = modifier
            .rotate(memo.rotationDegree.toFloat())
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(memo.shapeType.imageRes()),
            contentDescription = null,
            colorFilter = ColorFilter.tint(memo.memoColor()),
            modifier = Modifier.size((140 * scale).dp)
        )

        Text(
            text = memo.previewContent,
            modifier = Modifier
                .width((111 * scale).dp)
                .height((110 * scale).dp)
                .padding((8 * scale).dp),
            style = SoptTheme.typography.body14M.copy(
                fontSize = (14 * scale).sp,
                lineHeight = (22 * scale).sp
            ),
            color = SoptTheme.colors.onSurface800,
            textAlign = TextAlign.Center,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
