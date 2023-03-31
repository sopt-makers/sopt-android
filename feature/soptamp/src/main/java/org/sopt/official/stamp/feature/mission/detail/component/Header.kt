/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.mission.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
