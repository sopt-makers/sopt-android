/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.schedule.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.mds.components.tag.MdsTag
import org.sopt.official.mds.components.tag.MdsTagEmphasis
import org.sopt.official.mds.components.tag.MdsTagShape
import org.sopt.official.mds.components.tag.MdsTagSize
import org.sopt.official.mds.components.tag.MdsTagType
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun ScheduleItem(
    date: String,
    title: String,
    type: String,
    isRecentSchedule: Boolean = false,
) {
    val (event, tagType) = when (type) {
        "SEMINAR" -> "세미나" to MdsTagType.PRIMARY
        "JOINT_SEMINAR" -> "합동 세미나" to MdsTagType.PRIMARY
        "BREAK" -> "휴식" to MdsTagType.DEFAULT
        else -> "행사" to MdsTagType.SECONDARY
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        ScheduleIndicator(
            circleColor = if (isRecentSchedule) SoptTheme.colors.fg.neutral.bold else SoptTheme.colors.fg.neutral.ghost,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = date,
                color = SoptTheme.colors.fg.neutral.subtle,
                style = SoptTheme.typography.label4,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MdsTag(
                    text = event,
                    emphasis = MdsTagEmphasis.SUBTLE,
                    size = MdsTagSize.SMALL,
                    shape = MdsTagShape.RECT,
                    type = tagType
                )

                Text(
                    text = title,
                    color = SoptTheme.colors.fg.neutral.bold,
                    style = SoptTheme.typography.title4
                )
            }
        }
    }
}

@Preview
@Composable
private fun ScheduleItemPreview() {
    SoptTheme {
        ScheduleItem(
            date = "9월 28일 토요일",
            title = "1차 세미나",
            type = "세미나"
        )
    }
}
