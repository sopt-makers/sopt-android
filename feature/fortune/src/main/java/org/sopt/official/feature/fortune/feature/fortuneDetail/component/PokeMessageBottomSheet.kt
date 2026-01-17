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
package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.fortune.R.drawable.ic_checkbox_off
import org.sopt.official.feature.fortune.R.drawable.ic_checkbox_on

@Composable
fun PokeMessageBottomSheetScreen(
    isAnonymous: Boolean,
    selectedIndex: Int,
    onItemClick: (selectedIndex: Int, message: String) -> Unit,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(
                top = 24.dp,
                bottom = 12.dp,
            ),
    ) {
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "함께 보낼 메시지를 선택해주세요",
                style = typography.heading20B,
                color = colors.onSurface30,
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            Image(
                imageVector = if (isAnonymous) ImageVector.vectorResource(id = ic_checkbox_on)
                else ImageVector.vectorResource(id = ic_checkbox_off),
                contentDescription = "익명 체크박스",
                modifier = Modifier.clickable { onIconClick() },
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Text(
                text = "익명",
                style = typography.title16SB,
                color = colors.onSurface10,
            )
        }
        Spacer(modifier = Modifier.height(height = 12.dp))
        LazyColumn(contentPadding = PaddingValues(vertical = 4.dp)) {
            val messages = persistentListOf(
                "안녕하세요? I AM 35기에요",
                "친해지고 싶어서 DM 드려요 ^^~",
                "이야기 해보고 싶었어요!!",
                "모각작 하실래요?",
                "콕 \uD83D\uDC48",
            )

            itemsIndexed(messages) { index, message ->
                PokeMessageItem(
                    message = message,
                    isSelected = index == selectedIndex,
                    onItemClick = { onItemClick(index, message) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PokeMessageBottomSheetScreenPreview() {
    SoptTheme {
        PokeMessageBottomSheetScreen(
            isAnonymous = true,
            selectedIndex = 0,
            onItemClick = { _, _ -> },
            onIconClick = { },
        )
    }
}
