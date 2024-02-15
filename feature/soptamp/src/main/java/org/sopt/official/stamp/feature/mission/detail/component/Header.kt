/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.mission.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.R
import org.sopt.official.stamp.util.DefaultPreview
import org.sopt.official.stamp.designsystem.component.ratingbar.RatingBar
import org.sopt.official.stamp.designsystem.component.toolbar.ToolbarIconType
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.feature.ranking.getLevelTextColor
import org.sopt.official.stamp.feature.ranking.headerBackgroundColorOf

@Composable
fun Header(
    title: String,
    stars: Int,
    toolbarIconType: ToolbarIconType,
    isMe: Boolean,
    isCompleted: Boolean
) {
    Surface(
        modifier = Modifier
            .background(
                color = if ((!isMe || isCompleted) && toolbarIconType == ToolbarIconType.WRITE) {
                    headerBackgroundColorOf(stars)
                } else {
                    SoptTheme.colors.onSurface5
                },
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.background(SoptTheme.colors.onSurface5),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RatingBar(
                icon = R.drawable.ic_star,
                stars = stars,
                selectedColor = getLevelTextColor(rank = stars)
            )
            Text(
                text = title,
                style = SoptTheme.typography.sub1,
                color = SoptTheme.colors.onSurface90
            )
        }
    }
}

@DefaultPreview
@Composable
private fun HeaderPreview() {
    SoptTheme {
        Header(
            title = "오늘은 무슨 일을 할까?",
            stars = 2,
            toolbarIconType = ToolbarIconType.WRITE,
            isMe = true,
            isCompleted = false
        )
    }
}
