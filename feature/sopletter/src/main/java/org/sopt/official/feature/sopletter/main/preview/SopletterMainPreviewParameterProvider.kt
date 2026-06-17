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
package org.sopt.official.feature.sopletter.main.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.sopletter.main.contract.toMemoDetailDialogState
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import org.sopt.official.feature.sopletter.main.model.SopletterMemoColor
import org.sopt.official.feature.sopletter.main.model.SopletterMemoRotationType
import org.sopt.official.feature.sopletter.main.model.SopletterMemoUiModel
import org.sopt.official.sopletter.R

class SopletterMainPreviewParameterProvider : PreviewParameterProvider<SopletterMainUiState> {
    private val memoList = persistentListOf(
        SopletterMemoUiModel(
            id = 1L,
            message = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하세",
            shapeImageRes = R.drawable.ic_sopletter_memo_cloud,
            rotation = SopletterMemoRotationType.LEFT,
            memoColor = SopletterMemoColor.BLUE,
        ),
        SopletterMemoUiModel(
            id = 2L,
            message = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하세",
            shapeImageRes = R.drawable.ic_sopletter_memo_sharp,
            rotation = SopletterMemoRotationType.RIGHT,
            memoColor = SopletterMemoColor.MINT,
        ),
        SopletterMemoUiModel(
            id = 3L,
            message = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하세",
            shapeImageRes = R.drawable.ic_sopletter_memo_smooth,
            rotation = SopletterMemoRotationType.CENTER,
            memoColor = SopletterMemoColor.PINK,
        ),
        SopletterMemoUiModel(
            id = 4L,
            message = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하세",
            shapeImageRes = R.drawable.ic_sopletter_memo_point,
            rotation = SopletterMemoRotationType.LEFT,
            memoColor = SopletterMemoColor.YELLOW,
        ),
    )

    private val previewStates = listOf(
        SopletterMainUiState(
            generation = 38,
        ),
        SopletterMainUiState(
            generation = 38,
            memoList = memoList,
        ),
        SopletterMainUiState(
            generation = 38,
            memoList = memoList,
            selectedMemoDetail = memoList.first().toMemoDetailDialogState().copy(
                memoColor = memoList[1].memoColor,
                isMine = true,
                isLiked = true,
                date = "04.18",
                content = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요" +
                "녕하세안녕하세요녕하세하세안녕하세요녕하안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하안녕하세요안녕하세요" +
                    "안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요",
            ),
        ),
    )

    override val values: Sequence<SopletterMainUiState> = previewStates.asSequence()

    override fun getDisplayName(index: Int): String = when (index) {
        0 -> "Empty"
        1 -> "Memo List"
        else -> "Selected Memo"
    }
}
