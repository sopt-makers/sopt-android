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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.util.DefaultPreview

@Composable
fun DoubleOptionDialog(
    title: String,
    subTitle: String = "",
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .noRippleClickable { }
            .padding(horizontal = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = SoptTheme.colors.white,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp)
                    .background(
                        color = SoptTheme.colors.white,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = SoptTheme.typography.sub1,
                    color = SoptTheme.colors.onSurface90
                )
                if (subTitle.isNotBlank()) {
                    Text(
                        text = subTitle,
                        modifier = Modifier.padding(top = 6.dp),
                        style = SoptTheme.typography.caption3,
                        color = SoptTheme.colors.onSurface50,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(
                modifier = Modifier.background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = SoptTheme.colors.onSurface30,
                            shape = RoundedCornerShape(bottomStart = 10.dp)
                        )
                        .clickable { onCancel() }
                ) {
                    Text(
                        text = "취소",
                        color = SoptTheme.colors.onSurface70,
                        style = SoptTheme.typography.sub1,
                        modifier = Modifier.padding(vertical = 15.dp)
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = SoptTheme.colors.error200,
                            shape = RoundedCornerShape(bottomEnd = 10.dp)
                        )
                        .clickable { onConfirm() }
                ) {
                    Text(
                        text = "삭제",
                        color = SoptTheme.colors.white,
                        style = SoptTheme.typography.sub1,
                        modifier = Modifier.padding(vertical = 15.dp)
                    )
                }
            }
        }
    }
}

@DefaultPreview
@Composable
fun DoubleOptionDialogPreview() {
    SoptTheme {
        DoubleOptionDialog(
            title = "미션을 초기화 하시겠습니까?",
            subTitle = " 사진, 메모가 삭제되고\n전체 미션이 미완료상태로 초기화됩니다.",
            onConfirm = { },
            onCancel = { }
        )
    }
}
