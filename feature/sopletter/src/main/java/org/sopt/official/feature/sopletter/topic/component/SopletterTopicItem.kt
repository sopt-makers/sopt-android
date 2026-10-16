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
package org.sopt.official.feature.sopletter.topic.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.components.tag.MdsTag
import org.sopt.official.mds.components.tag.MdsTagEmphasis
import org.sopt.official.mds.components.tag.MdsTagShape
import org.sopt.official.mds.components.tag.MdsTagSize
import org.sopt.official.mds.components.tag.MdsTagType
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun SopletterTopicItem(
    topicTitle: String,
    topicSubTitle: String?,
    isHot: Boolean,
    isNew: Boolean,
    unReadCount: Int,
    onTopicClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.bg.neutral.subtle,
                shape = RoundedCornerShape(10.dp)
            )
            .noRippleClickable(onClick = onTopicClick)
            .padding(vertical = 12.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = topicTitle,
                style = SoptTheme.typography.label2,
                color = SoptTheme.colors.fg.neutral.bold,
            )

            if (topicSubTitle != null) {
                Text(
                    text = topicSubTitle,
                    style = SoptTheme.typography.label3,
                    color = SoptTheme.colors.fg.neutral.default,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (isHot) {
            SopletterTopicTag(
                text = "HOT🔥",
                type = MdsTagType.PRIMARY
            )
        }

        if (isNew) {
            SopletterTopicTag(
                text = "NEW!",
                type = MdsTagType.SECONDARY
            )
        }

        if (unReadCount > 0) {
            if (!isHot && !isNew) {
                Text(
                    text = "${unReadCount}개",
                    style = SoptTheme.typography.label3,
                    color = SoptTheme.colors.fg.brand.default,
                )
            }

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = SoptTheme.colors.fg.success.default,
                        shape = CircleShape,
                    )
            )
        }

        Icon(
            imageVector = ImageVector.vectorResource(MdsIcons.chevronRightOutlined),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 2.dp)
                .size(20.dp),
            tint = SoptTheme.colors.fg.neutral.bold,
        )
    }
}

@Composable
private fun SopletterTopicTag(
    text: String,
    type: MdsTagType,
    modifier: Modifier = Modifier,
) {
    MdsTag(
        text = text,
        type = type,
        emphasis = MdsTagEmphasis.SUBTLE,
        size = MdsTagSize.SMALL,
        shape = MdsTagShape.PILL,
        modifier = modifier
    )
}

@Preview
@Composable
private fun SopletterTopicItemPreview() {
    SoptTheme {
        SopletterTopicItem(
            topicTitle = "38기 회고",
            topicSubTitle = "자유롭게 수다 나누는 방방",
            isHot = true,
            isNew = true,
            unReadCount = 2,
            onTopicClick = {}
        )
    }
}
