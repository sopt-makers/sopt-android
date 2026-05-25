package org.sopt.official.feature.sopletter.main.mapper

import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.feature.sopletter.main.model.SopletterMemoUiModel

// TODO: 임시 로직 추후 서버 연결시 수정 예정
internal fun SopletterMemoUiModel.toDetailDialogState(): SopletterMemoDetailDialogContract.State =
    SopletterMemoDetailDialogContract.State(
        memoId = id,
        memoColor = memoColor,
        writerName = "익명의 솝레터",
        isMine = false,
        isLiked = false,
        likeCount = 32,
        date = "mm.dd",
        content = message,
    )
