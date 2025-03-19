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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.SoptTheme
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
        modifier = modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(46.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .width(134.dp)
                .height(54.dp)
                .weight(1f)
                .background(
                    SoptTheme.colors.purple300,
                    shape = RoundedCornerShape(topStart = 46.dp, bottomStart = 46.dp)
                )
                .clip(RoundedCornerShape(topStart = 46.dp, bottomStart = 46.dp))
                .clickable(onClick = onClickFirstOption)
                .padding(top = 12.dp, bottom = 12.dp, start = 14.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.trophy),
                contentDescription = "Extended Floating Action Button Trophy Icon",
                tint = Color.White
            )
            Text(
                text = option1,
                color = Color.White,
                style = SoptTheme.typography.h2,
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier
                .width(134.dp)
                .height(54.dp)
                .weight(1f)
                .background(
                    SoptTheme.colors.pink300,
                    shape = RoundedCornerShape(topEnd = 46.dp, bottomEnd = 46.dp)
                )
                .clip(RoundedCornerShape(topEnd = 46.dp, bottomEnd = 46.dp))
                .clickable(onClick = onClickSecondOption)
                .padding(top = 12.dp, bottom = 12.dp, start = 5.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.trophy),
                contentDescription = "Extended Floating Action Button Trophy Icon",
                tint = Color.White
            )
            Text(
                text = option2,
                color = Color.White,
                style = SoptTheme.typography.h2,
                fontSize = 18.sp
            )
        }
    }
}

@DefaultPreview
@Composable
private fun SoptampSegmentedFloatingButtonPreview() {
    SoptTheme {
        SoptampSegmentedFloatingButton(option1 = "랭킹 보기", option2 = "전체 랭킹") { }
    }
}
