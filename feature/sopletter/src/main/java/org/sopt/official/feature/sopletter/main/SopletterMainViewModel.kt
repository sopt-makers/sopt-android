package org.sopt.official.feature.sopletter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import javax.inject.Inject

@HiltViewModel
class SopletterMainViewModel @Inject constructor(
) : ViewModel(), SopletterMemoDetailDialogContract.Actions {
    private val _uiState = MutableStateFlow(SopletterMainUiState())
    val uiState: StateFlow<SopletterMainUiState> = _uiState.asStateFlow()
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    fun updateSelectMemoDetail(memo: SopletterMemoDetailDialogContract.State) {
        _uiState.update { state ->
            state.copy(selectedMemoDetail = memo)
        }
    }

    override fun onLikeClick() {
        val selectedMemoDetail = _uiState.value.selectedMemoDetail ?: return

        if (selectedMemoDetail.isMine) {
            _snackbarMessage.tryEmit("내가 작성한 솝레터에는 좋아요를 누를 수 없어요.")
            return
        }

        _uiState.update { state ->
            state.copy(
                selectedMemoDetail = selectedMemoDetail.copy(
                    isLiked = !selectedMemoDetail.isLiked,
                    likeCount = if (selectedMemoDetail.isLiked) {
                        selectedMemoDetail.likeCount - 1
                    } else {
                        selectedMemoDetail.likeCount + 1
                    },
                ),
            )
        }
    }

    override fun onEditClick() = Unit

    override fun onDeleteClick() = Unit

    override fun onDismissClick() {
        _uiState.update { state ->
            state.copy(selectedMemoDetail = null)
        }
    }

    fun refreshMemoList() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true)
            }

            // TODO Refresh 로직

            _uiState.update { state ->
                state.copy(isLoading = false)
            }
        }
    }
}
