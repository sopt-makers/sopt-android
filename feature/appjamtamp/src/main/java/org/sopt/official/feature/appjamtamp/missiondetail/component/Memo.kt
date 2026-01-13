/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray500
import org.sopt.official.designsystem.PretendardRegular
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun Memo(
    value: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    isEditable: Boolean,
) {
    val backgroundColor = SoptTheme.colors.onSurface900
    val isEmpty = remember(value) { value.isEmpty() }

    val modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 100.dp)
        .clip(RoundedCornerShape(12.dp))

    val modifierWithBorder =
        remember(isEmpty, isEditable) {
            if (isEmpty || !isEditable) {
                modifier
            } else {
                modifier
                    .border(
                        width = 1.dp,
                        color = Gray500,
                        shape = RoundedCornerShape(12.dp),
                    )
            }
        }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifierWithBorder,
        shape = RoundedCornerShape(12.dp),
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                errorContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = SoptTheme.colors.onSurface50,
                disabledTextColor = SoptTheme.colors.onSurface50,
                unfocusedTextColor = SoptTheme.colors.onSurface50,
                focusedPlaceholderColor = SoptTheme.colors.onSurface300,
            ),
        textStyle = SoptTheme.typography.body14R.copy(fontFamily = PretendardRegular),
        placeholder = {
            Text(
                text = placeHolder,
                style = SoptTheme.typography.body14R.copy(fontFamily = PretendardRegular),
                color = SoptTheme.colors.onSurface300
            )
        },
        enabled = isEditable,
    )
}

@Preview
@Composable
private fun MemoPreview() {
    SoptTheme {
        Memo(
            value = "안돼",
            onValueChange = {},
            placeHolder = "",
            isEditable = false,
        )
    }
}
