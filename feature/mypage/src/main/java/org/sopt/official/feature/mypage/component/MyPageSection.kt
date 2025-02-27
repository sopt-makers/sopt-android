/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.sopt.official.designsystem.Gray400
import org.sopt.official.designsystem.Gray900
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.model.MyPageUiModel

@Composable
fun MyPageSection(items: ImmutableList<MyPageUiModel>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .background(
                color = Gray900,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(top = 16.dp, bottom = 5.dp)
    ) {
        items.forEach { item ->
            when (item) {
                is MyPageUiModel.Header -> {
                    Text(
                        text = item.title,
                        style = SoptTheme.typography.label12SB,
                        color = Gray400,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(23.dp))
                }

                is MyPageUiModel.MyPageItem -> {
                    MyPageItem(
                        text = item.title,
                        onClick = item.onItemClick
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                }
            }
        }
    }
}
