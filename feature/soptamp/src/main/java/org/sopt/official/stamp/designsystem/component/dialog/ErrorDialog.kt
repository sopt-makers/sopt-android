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
package org.sopt.official.stamp.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.util.DefaultPreview

@Composable
fun ErrorDialog(
    title: String,
    content: String? = null,
    retryButtonText: String = "확인",
    onRetry: () -> Unit = {}
) {
    var openDialog by remember {
        mutableStateOf(true)
    }
    if (openDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .noRippleClickable { }
        ) {
            AlertDialog(
                backgroundColor = SoptTheme.colors.onSurface700,
                shape = RoundedCornerShape(10.dp),
                onDismissRequest = { openDialog = false },
                title = {
                    Text(
                        text = title,
                        style = SoptTheme.typography.heading16B,
                        color = SoptTheme.colors.onSurface10,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    content?.let {
                        Text(
                            text = it,
                            style = SoptTheme.typography.body14M,
                            color = SoptTheme.colors.onSurface30,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                buttons = {
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 7.dp, end = 7.dp,
                                bottom = 10.dp, top = 14.dp
                            )
                            .fillMaxWidth()
                            .background(
                                color = SoptTheme.colors.onSurface10,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .noRippleClickable {
                                onRetry()
                                openDialog = false
                            }
                            .padding(vertical = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = retryButtonText,
                            style = SoptTheme.typography.title14SB,
                            color = SoptTheme.colors.onSurface950
                        )
                    }
                }
            )
        }
    }
}

@DefaultPreview
@Composable
fun PreviewErrorDialog() {
    SoptTheme {
        ErrorDialog(
            title = "네트워크가 원할하지 않습니다.",
            content = "인터넷 연결을 확인하고 다시 시도해 주세요."
        )
    }
}
