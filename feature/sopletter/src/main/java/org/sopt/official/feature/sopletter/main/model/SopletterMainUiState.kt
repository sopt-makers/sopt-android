package org.sopt.official.feature.sopletter.main.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SopletterMainUiState(
    val generation: Int = 0,
    val memoList: ImmutableList<SopletterMemoUiModel> = persistentListOf(),
    val isLoading: Boolean = false,
)
