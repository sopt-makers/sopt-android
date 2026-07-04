package org.sopt.official.feature.sopletter.print

import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarType

data class SopletterPrintUiState(
    val generation: Int = 0,
    val topicTitle: String = "",
    val totalCount: Int = 0,
    val previewMemoList: List<SopletterMessage> = emptyList(),
    val fullMemoList: List<SopletterMessage>? = null,
    val isShowPrintDialog: Boolean = false,
    val isSaving: Boolean = false,
    val isCaptureRequested: Boolean = false
)

sealed interface SopletterPrintSideEffect {
    data class ShowSnackbar(
        val message: String,
        val type: SopletterSnackbarType
    ) : SopletterPrintSideEffect

    data object NavigateBack : SopletterPrintSideEffect
}