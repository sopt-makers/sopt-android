package org.sopt.official.feature.sopletter.main.contract

import androidx.compose.runtime.Immutable
import org.sopt.official.feature.sopletter.main.model.SopletterMemoColor

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
