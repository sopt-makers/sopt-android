package org.sopt.official.feature.sopletter.main.model

import androidx.compose.runtime.Stable

@Stable
data class SopletterMemoDetailDialogState(
    val memoId: Long,
    val memoColor: SopletterMemoColor,
    val writerName: String,
    val isMine: Boolean,
    val isLiked: Boolean,
    val likeCount: Long,
    val date: String,
    val content: String,
    val event: Event,
) {
    @Stable
    data class Event(
        val onLikeClick: () -> Unit,
        val onEditClick: () -> Unit,
        val onDeleteClick: () -> Unit,
        val onDismissClick: () -> Unit,
    )
}
