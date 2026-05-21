package org.sopt.official.feature.sopletter.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import org.sopt.official.feature.sopletter.main.model.SopletterMemoDetailDialogState
import javax.inject.Inject

@HiltViewModel
class SopletterMainViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(SopletterMainUiState())
    val uiState: StateFlow<SopletterMainUiState> = _uiState.asStateFlow()

    fun updateSelectMemoDetail(memo: SopletterMemoDetailDialogState) {
        _uiState.update { state ->
            state.copy(selectedMemoDetail = memo)
        }
    }

    fun clearSelectedMemo() {
        _uiState.update { state ->
            state.copy(selectedMemoDetail = null)
        }
    }
}
