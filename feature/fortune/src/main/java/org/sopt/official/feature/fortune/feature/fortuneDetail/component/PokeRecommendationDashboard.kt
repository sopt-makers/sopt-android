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
import androidx.compose.foundation.border
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Blue400
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray30
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.R
import org.sopt.official.feature.fortune.component.UrlImage

@Composable
internal fun PokeRecommendationDashboard(
    profile: String,
    name: String,
    generation: String,
    onPokeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TodayFortuneBox(
        content = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
            ) {
                Text(
                    text = "콕 찌르기",
                    style = SoptTheme.typography.body14R,
                    color = Gray100,
                )
                Text(
                    text = "행운이 2배가 될 솝트인을 찔러보세요",
                    style = SoptTheme.typography.title18SB,
                    color = Gray30,
                )
                Spacer(modifier = Modifier.height(height = 20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    UrlImage(
                        url = profile,
                        contentDescription = "profileImage",
                        modifier = Modifier.size(size = 70.dp).border(
                            width = 2.dp,
                            color = Blue400,
                            shape = RoundedCornerShape(size = 100.dp),
                        )
                    )
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    Column {
                        Text(
                            text = name,
                            style = SoptTheme.typography.body18M,
                            color = Gray30,
                        )
                        Text(
                            text = generation,
                            style = SoptTheme.typography.label12SB,
                            color = Gray300,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = onPokeClick,
                        modifier = Modifier
                            .size(size = 44.dp)
                            .background(
                                color = Gray10,
                                shape = RoundedCornerShape(size = 18.dp),
                            ),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_poke),
                            contentDescription = "콕 찌르기",
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun PokeRecommendationDashboardPreview() {
    SoptTheme {
        PokeRecommendationDashboard(
            profile = "123",
            name = "이현우",
            onPokeClick = { },
            generation = "1100기 iOS",
        )
    }
}