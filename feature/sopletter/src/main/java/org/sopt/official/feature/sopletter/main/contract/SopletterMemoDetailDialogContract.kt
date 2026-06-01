package org.sopt.official.feature.sopletter.main.contract

import androidx.compose.runtime.Immutable
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract.State
import org.sopt.official.feature.sopletter.main.model.SopletterMemoColor
import org.sopt.official.feature.sopletter.main.model.SopletterMemoUiModel

interface SopletterMemoDetailDialogContract {
    @Immutable
    data class State(
        val memoId: Long,
        val memoColor: SopletterMemoColor,
        val writerName: String,
        val isMine: Boolean,
        val isLiked: Boolean,
        val likeCount: Long,
        val date: String,
        val content: String,
    )

    interface Actions {
        fun onLikeClick()
        fun onEditClick()
        fun onDeleteClick()
        fun onDismissClick()
    }
}

// TODO: 임시 로직 추후 서버 연결시 수정 예정
internal fun SopletterMemoUiModel.toMemoDetailDialogState(): State =
    State(
        memoId = id,
        memoColor = memoColor,
        writerName = "익명의 솝레터",
        isMine = false,
        isLiked = false,
        likeCount = 32,
        date = "mm.dd",
        content = message,
    )
