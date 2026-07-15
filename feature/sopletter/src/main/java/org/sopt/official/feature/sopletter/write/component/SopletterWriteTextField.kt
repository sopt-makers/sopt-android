/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.sopletter.write.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.component.verticalScrollbar

@Composable
fun SopletterWriteTextBox(
    userName: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    maxLength: Int = 350,
    onLimitExceeded: () -> Unit = {}
) {
    val isOverLimit = state.text.length > maxLength
    val borderColor = if (isOverLimit) SoptTheme.colors.error else Color.Transparent
    val counterColor = if (isOverLimit) SoptTheme.colors.error else SoptTheme.colors.onSurface300

    val scrollState = rememberScrollState()

    LaunchedEffect(state) {
        snapshotFlow { state.text.length > maxLength }
            .drop(1)
            .filter { it }
            .collect { onLimitExceeded() }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = SoptTheme.colors.onSurface700, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .padding(
                top = 28.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = userName,
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.onSurface10
        )

        BasicTextField(
            state = state,
            scrollState = scrollState,
            textStyle = SoptTheme.typography.body16R.copy(color = SoptTheme.colors.primary),
            cursorBrush = SolidColor(SoptTheme.colors.onSurface10),
            lineLimits = TextFieldLineLimits.MultiLine(),
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f, fill = false)
                .verticalScrollbar(
                    scrollState = scrollState,
                    thumbWidth = 4.dp,
                    thumbHeight = 100.dp,
                    thumbColor = SoptTheme.colors.onSurface300.copy(alpha = 0.5f)
                ),
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = "나와 같은 기수의 솝트인들에게 전하고 싶은 말을 자유롭게 적어보세요.",
                            style = SoptTheme.typography.body16R,
                            color = SoptTheme.colors.onSurface400
                        )
                    }
                    innerTextField()
                }
            }
        )

        Text(
            text = "${state.text.length}/${maxLength}자",
            style = SoptTheme.typography.body14M,
            color = counterColor,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SopletterWriteTextBoxPreview() {
    val state = rememberTextFieldState()
    SoptTheme {
        SopletterWriteTextBox(
            userName = "익명의 무무",
            state = state
        )
    }
}
