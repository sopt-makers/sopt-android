/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.stamp.R
import org.sopt.official.stamp.util.DefaultPreview

@Composable
fun SoptampSegmentedFloatingButton(
    option1: String,
    option2: String,
    modifier: Modifier = Modifier,
    onClickFirstOption: () -> Unit = {},
    onClickSecondOption: () -> Unit = {},
) {
    Row(
        modifier = modifier.clip(shape = CircleShape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier =
                Modifier
                    .background(SoptTheme.colors.primary)
                    .clickable(onClick = onClickFirstOption)
                    .padding(vertical = 11.dp)
                    .padding(start = 13.dp, end = 4.dp)
                    .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.trophy),
                contentDescription = "Extended Floating Action Button Trophy Icon",
                tint = SoptTheme.colors.onSurface,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = option1,
                color = SoptTheme.colors.onSurface,
                style = SoptTheme.typography.heading18B,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier =
                Modifier
                    .background(SoptTheme.colors.onSurface)
                    .clickable(onClick = onClickSecondOption)
                    .padding(vertical = 11.dp)
                    .padding(start = 4.dp, end = 13.dp)
                    .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.trophy),
                contentDescription = "Extended Floating Action Button Trophy Icon",
                tint = SoptTheme.colors.primary,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = option2,
                color = SoptTheme.colors.primary,
                style = SoptTheme.typography.heading18B,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@DefaultPreview
@Composable
private fun SoptampSegmentedFloatingButtonPreview() {
    SoptTheme {
        SoptampSegmentedFloatingButton(option1 = "37기 랭킹", option2 = "파트별 랭킹") { }
    }
}
