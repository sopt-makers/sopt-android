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
package org.sopt.official.feature.sopletter.write.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun SopletterTopicSelectField(
    selectedTopicTitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = selectedTopicTitle ?: "주제를 선택해 주세요.",
        style = SoptTheme.typography.body1,
        color = if (selectedTopicTitle != null) SoptTheme.colors.fg.neutral.bold else SoptTheme.colors.fg.neutral.subtle,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.bg.neutral.ghost,
                shape = RoundedCornerShape(SoptTheme.radius.r12),
            )
            .noRippleClickable(onClick = onClick)
            .padding(
                horizontal = SoptTheme.spacing.s20,
                vertical = SoptTheme.spacing.s10,
            ),
    )
}

@Preview
@Composable
private fun SopletterTopicSelectFieldPreview() {
    SoptTheme {
        SopletterTopicSelectField(
            selectedTopicTitle = null,
            onClick = {},
        )
    }
}
