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
package org.sopt.official.feature.sopletter.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.TwoButtonDialog

@Composable
fun SopletterPrintDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onPrintConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TwoButtonDialog(
        onDismiss = onDismissRequest,
        positiveButtonText = "출력",
        negativeButtonText = "취소",
        onPositiveClick = onPrintConfirm,
        onNegativeClick = onDismissRequest,
        buttonVerticalPadding = 13.dp,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "솝레터 출력하기",
                style = SoptTheme.typography.title18SB,
                color = SoptTheme.colors.onSurface10,
            )

            Text(
                text = "${title}의 모든 메시지가 하나의 이미지로 출력돼요. 솝레터를 출력하여 우리 기수의 이야기를 공유해보세요!",
                style = SoptTheme.typography.body14R,
                color = SoptTheme.colors.onSurface100,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 812)
@Composable
private fun SopletterPrintDialogPreview() {
    SoptTheme {
        SopletterPrintDialog(
            title = "nn기 솝레터",
            onDismissRequest = {},
            onPrintConfirm = {},
        )
    }
}
