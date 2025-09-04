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
package org.sopt.official.designsystem.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.designsystem.SoptTheme

@Composable
fun TwoButtonDialog(
    onDismiss: () -> Unit,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
    content: @Composable () -> Unit,
) {
    SoptBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier,
        properties = properties
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            content()

            Row(
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DialogButton(
                    text = negativeButtonText,
                    onClick = onNegativeClick,
                    colors = ButtonColors(
                        containerColor = SoptTheme.colors.onSurface600,
                        contentColor = SoptTheme.colors.primary,
                        disabledContainerColor = SoptTheme.colors.onSurface600,
                        disabledContentColor = SoptTheme.colors.primary
                    ),
                    modifier = Modifier.weight(1f)
                )

                DialogButton(
                    text = positiveButtonText,
                    onClick = onPositiveClick,
                    colors = ButtonColors(
                        containerColor = SoptTheme.colors.primary,
                        contentColor = SoptTheme.colors.onSurface,
                        disabledContainerColor = SoptTheme.colors.primary,
                        disabledContentColor = SoptTheme.colors.onSurface
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(vertical = 11.dp),
        colors = colors,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = SoptTheme.typography.label16SB,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 812)
@Composable
private fun TwoButtonDialogPreview() {
    SoptTheme {
        TwoButtonDialog(
            onDismiss = {},
            positiveButtonText = "업데이트 하기",
            negativeButtonText = "닫기",
            onPositiveClick = {},
            onNegativeClick = {}
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "다이얼로그 테스트",
                    style = SoptTheme.typography.title18SB,
                    color = SoptTheme.colors.primary
                )
                Text(
                    text = "짜잔! 이건 버튼이 두개인 다이얼로그지롱",
                    style = SoptTheme.typography.body14R,
                    color = SoptTheme.colors.onSurface100
                )
            }
        }
    }
}
