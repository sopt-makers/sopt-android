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
package org.sopt.official.feature.soptlog.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography

@Composable
fun SoptLogErrorDialog(
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "네트워크가 원활하지 않습니다.",
    content: String = "인터넷 연결을 확인하고 다시 시도해 주세요."
) {
    Box(
        contentAlignment = Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.background.copy(alpha = 0.55f)),
    ) {
        Dialog(onDismissRequest = onCheckClick) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        color = colors.onBackground,
                        shape = RoundedCornerShape(size = 10.dp),
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    Spacer(modifier = Modifier.height(height = 26.dp))
                    Text(
                        text = title,
                        style = typography.heading16B,
                        color = colors.onSurface800,
                    )
                    Spacer(modifier = Modifier.height(height = 24.dp))
                    Text(
                        text = content,
                        style = typography.body14M,
                        color = colors.onSurface100,
                    )
                    Spacer(modifier = Modifier.height(height = 34.dp))
                    Button(
                        onClick = onCheckClick,
                        shape = RoundedCornerShape(size = 10.dp),
                        contentPadding = PaddingValues(
                            horizontal = 128.dp,
                            vertical = 12.dp,
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = Black),
                    ) {
                        Text(
                            text = "확인",
                            style = typography.body14R,
                            color = colors.onSurface10,
                        )
                    }
                    Spacer(modifier = Modifier.height(height = 10.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SoptLogErrorDialogPreview() {
    SoptTheme {
        SoptLogErrorDialog(
            onCheckClick = { },
        )
    }
}
