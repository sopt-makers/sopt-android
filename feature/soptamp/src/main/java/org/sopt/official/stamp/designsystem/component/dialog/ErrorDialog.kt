/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
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
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.SoptTheme
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
                onDismissRequest = { openDialog = false },
                title = {
                    Text(
                        text = title,
                        style = SoptTheme.typography.sub1,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    content?.let {
                        Text(
                            text = it,
                            style = SoptTheme.typography.caption3,
                            color = SoptTheme.colors.onSurface50,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                buttons = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFFF8080))
                            .noRippleClickable {
                                onRetry()
                                openDialog = false
                            }
                            .padding(vertical = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = retryButtonText,
                            style = SoptTheme.typography.sub1,
                            color = Color.White
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
