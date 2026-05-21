package org.sopt.official.feature.sopletter.main.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.sopletter.main.mapper.toDetailDialogState
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
            selectedMemoDetail = memoList.first().toDetailDialogState().copy(
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
