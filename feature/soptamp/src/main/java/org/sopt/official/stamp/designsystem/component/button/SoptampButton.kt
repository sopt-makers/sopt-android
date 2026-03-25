/*
 * MIT License
 * Copyright 2024-2026 SOPT - Shout Our Passion Together
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable

/**
 * SOPTAMP에서 사용하는 기본 액션 버튼
 *
 * @param text 버튼에 표시할 텍스트
 * @param onClicked 버튼 클릭 시 실행할 콜백
 * @param modifier 버튼에 적용할 [Modifier]
 * @param isEnabled 버튼 활성화 여부 (false 시 버튼 색상 dim 처리)
 */
@Composable
fun SoptampButton(
    text: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = if (isEnabled) SoptTheme.colors.primary else SoptTheme.colors.onSurface300,
                    shape = RoundedCornerShape(9.dp),
                )
                .noRippleClickable(onClick = { if (isEnabled) onClicked() }),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = text,
            style = SoptTheme.typography.heading18B,
            color = SoptTheme.colors.onSurface,
        )
    }
}
