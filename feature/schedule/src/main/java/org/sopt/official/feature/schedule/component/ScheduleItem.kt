/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Blue400
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme

@Composable
fun ScheduleItem(
    date: String,
    title: String,
    type: String,
    isRecentSchedule: Boolean = false,
) {
    val (event, containerColor, textColor) = remember {
        when (type) {
            "SEMINAR" -> Triple("세미나", Orange400.copy(alpha = 0.2f), Orange400)
            else -> Triple("행사", Blue400.copy(alpha = 0.2f), Blue400)
        }
    }


    Row {
        VerticalDividerWithCircle(
            circleColor = if (isRecentSchedule) SoptTheme.colors.onSurface10 else SoptTheme.colors.onSurface500,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = date,
                color = SoptTheme.colors.primary,
                style = SoptTheme.typography.body14M,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event,
                    color = textColor,
                    modifier = Modifier
                        .background(containerColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    style = SoptTheme.typography.label11SB,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = SoptTheme.colors.onSurface10,
                    style = SoptTheme.typography.heading18B
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
