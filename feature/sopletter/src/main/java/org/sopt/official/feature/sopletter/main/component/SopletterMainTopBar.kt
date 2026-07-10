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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.sopletter.R

@Composable
internal fun SopletterMainTopBar(
    title: String,
    isTopicDetail: Boolean,
    isDownloadBtnVisible: Boolean,
    onBackClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onReportClick: () -> Unit,
    onTopicClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    if (isTopicDetail) R.drawable.ic_btn_arrow_left else R.drawable.ic_close_32,
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(32.dp)
                    .noRippleClickable(onClick = onBackClick),
            )

            Text(
                text = title,
                style = SoptTheme.typography.heading18B,
                color = SoptTheme.colors.onSurface10,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (isDownloadBtnVisible) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_download_32),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(32.dp)
                        .noRippleClickable(onClick = onDownloadClick),
                )
            }

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_alert_32),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(32.dp)
                    .noRippleClickable(onClick = onReportClick),
            )

            if (onTopicClick != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_topic_32),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(32.dp)
                        .noRippleClickable(onClick = onTopicClick),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SopletterMainTopBarPreview() {
    SoptTheme {
        SopletterMainTopBar(
            title = "38기 솝레터",
            isTopicDetail = false,
            isDownloadBtnVisible = true,
            onBackClick = {},
            onDownloadClick = {},
            onReportClick = {},
            onTopicClick = {},
        )
    }
}
