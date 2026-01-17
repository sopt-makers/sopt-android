/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.fortune.R.drawable.ic_poke

@Composable
fun PokeRecommendationDashboard(
    profile: String,
    name: String,
    userDescription: String,
    isEnabled: Boolean,
    onPokeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TodayFortuneBox(content = {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 20.dp),
        ) {
            Text(
                text = "콕 찌르기",
                style = typography.body14R,
                color = colors.onSurface100,
            )
            Text(
                text = "행운이 2배가 될 솝트인을 찔러보세요",
                style = typography.title18SB,
                color = colors.onSurface30,
            )
            Spacer(modifier = Modifier.height(height = 28.dp))
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                PokeRecommendationUserProfileImage(
                    profile = profile,
                    modifier = Modifier.size(size = 72.dp)
                        .clip(shape = RoundedCornerShape(size = 100.dp))
                        .clickable { onProfileClick() },
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                Column(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(end = 30.dp),
                ) {
                    Text(
                        text = name,
                        style = typography.body18M,
                        overflow = Ellipsis,
                        maxLines = 1,
                        color = colors.onSurface30,
                    )
                    Text(
                        text = userDescription,
                        style = typography.label12SB,
                        color = colors.onSurface300,
                    )
                }
                Box(
                    contentAlignment = Center,
                    modifier = Modifier
                        .size(size = 44.dp)
                        .background(
                            color = if (isEnabled) colors.onSurface10 else colors.onSurface700,
                            shape = RoundedCornerShape(size = 18.dp),
                        ).clickable { if (isEnabled) onPokeClick() },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = ic_poke),
                        contentDescription = "콕 찌르기",
                    )
                }
            }
        }
    })
}

@Preview
@Composable
private fun PokeRecommendationDashboardPreview() {
    SoptTheme {
        PokeRecommendationDashboard(
            profile = "123",
            name = "이현우이현우이현우이현우이현우",
            isEnabled = true,
            onPokeClick = { },
            onProfileClick = { },
            userDescription = "1100기 iOS",
        )
    }
}
