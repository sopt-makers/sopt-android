package org.sopt.official.feature.sopletter.main.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.sopletter.main.model.SopletterMemoDetailDialogState

data class SopletterMainUiState(
    val generation: Int = 0,
    val memoList: ImmutableList<SopletterMemoUiModel> = persistentListOf(),
    val selectedMemoDetail: SopletterMemoDetailDialogState? = null,
    val isLoading: Boolean = false,
)
