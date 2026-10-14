/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.mds.components.dialog.MdsDialog
import org.sopt.official.mds.components.dialog.MdsDialogType
import org.sopt.official.mds.theme.SoptTheme

/**
 * 네트워크 연결 불안정 상황에서 사용하는 공통 에러 다이얼로그
 *
 * 기본적으로 네트워크 장애 안내 문구와 단일 확인 버튼을 제공
 *
 * 동작 규칙:
 * - 다이얼로그 바깥 영역 탭 또는 뒤로가기 dismiss 시 [onConfirm]이 호출됩니다.
 * - 확인 버튼 클릭 시에도 [onConfirm]이 호출됩니다.
 *
 * @param onConfirm 확인 버튼 클릭 또는 dismiss 시 실행할 콜백
 * @param modifier 다이얼로그 컨테이너에 적용할 [Modifier]
 * @param title 다이얼로그 제목 (기본값: 네트워크 오류 안내)
 * @param content 다이얼로그 본문 설명 문구
 * @param buttonText 단일 버튼 텍스트 (기본값: 확인)
 */

@Composable
fun NetworkErrorDialog(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "네트워크가 원활하지 않습니다.",
    content: String = "인터넷 연결을 확인하고 다시 시도해 주세요.",
    buttonText: String = "확인",
) {
    MdsDialog(
        title = title,
        description = content,
        type = MdsDialogType.INFORMATION,
        positiveButtonText = buttonText,
        onPositiveButtonClick = onConfirm,
        onDismiss = onConfirm
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun NetworkErrorDialogPreview() {
    SoptTheme {
        NetworkErrorDialog(
            onConfirm = { },
        )
    }
}
