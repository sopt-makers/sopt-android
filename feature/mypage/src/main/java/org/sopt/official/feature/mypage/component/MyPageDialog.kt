/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray600
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White

@Composable
fun MyPageDialog(
    onDismissRequest: () -> Unit,
    title: String,
    subTitle: String,
    negativeText: String,
    positiveText: String,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    onPositiveButtonClick: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 25.dp)
                .background(
                    color = Gray700,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(top = 26.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                color = White,
                style = SoptTheme.typography.heading16B
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = subTitle,
                color = Gray100,
                style = SoptTheme.typography.body14M,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(34.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MyPageButton(
                    modifier = Modifier.weight(1f),
                    paddingVertical = 9.dp,
                    onClick = onDismissRequest,
                    containerColor = Gray600,
                    contentColor = Gray10,
                ) {
                    Text(
                        text = negativeText,
                        style = SoptTheme.typography.body14M
                    )
                }
                MyPageButton(
                    modifier = Modifier.weight(1f),
                    paddingVertical = 9.dp,
                    onClick = onPositiveButtonClick,
                ) {
                    Text(
                        text = positiveText,
                        style = SoptTheme.typography.body14M
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageDialogPreview() {
    SoptTheme {
        MyPageDialog(
            onDismissRequest = {},
            title = "미션을 초기화 하실건가요?",
            subTitle = "사진, 메모가 삭제되고\n전체 미션이 미완료상태로 초기화됩니다.",
            negativeText = "취소",
            positiveText = "초기화"
        )
    }
}
