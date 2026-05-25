package org.sopt.official.feature.sopletter.main.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract

@Immutable
data class SopletterMainUiState(
    val generation: Int = 0,
    val memoList: ImmutableList<SopletterMemoUiModel> = persistentListOf(),
    val selectedMemoDetail: SopletterMemoDetailDialogContract.State? = null,
    val isLoading: Boolean = false,
)
