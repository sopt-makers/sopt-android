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
package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.Orange500
import org.sopt.official.designsystem.Orange800
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.home.R

@Composable
fun HomeFloatingButton(
    imageUrl: String,
    shortTitle: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Orange400)
            .clickable(onClick = onClick)
            .padding(9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        UrlImage(
            url = imageUrl,
            modifier = Modifier
                .size(35.dp)
                .background(color = Orange500, shape = CircleShape)
                .padding(5.dp)
        )

        Column {
            Text(
                text = shortTitle,
                style = SoptTheme.typography.body13M,
                color = Orange800,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buttonText,
                    style = SoptTheme.typography.label14SB,
                    color = SoptTheme.colors.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
                    tint = SoptTheme.colors.onSurface,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
