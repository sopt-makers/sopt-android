package org.sopt.official.feature.sopletter.write

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
import org.sopt.official.feature.sopletter.write.model.SopletterSnackbarType
import org.sopt.official.feature.sopletter.write.model.SopletterWriteSideEffect
import org.sopt.official.feature.sopletter.write.model.SopletterWriteUiState
import org.sopt.official.sopletter.repository.SopletterWriteRepository
import javax.inject.Inject

@HiltViewModel
class SopletterWriteViewModel @Inject constructor(
    private val sopletterWriteRepository: SopletterWriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SopletterWriteUiState())
    val uiState: StateFlow<SopletterWriteUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SopletterWriteSideEffect>()
    val sideEffect: SharedFlow<SopletterWriteSideEffect> = _sideEffect.asSharedFlow()

    private var currentTopicId: Long? = 1 //TODO : topicId를 받아오는 것으로 변경예정

    fun postSopletter(content: String) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            sopletterWriteRepository.postSopletter(
                topicId = currentTopicId,
                content = content
            ).onSuccess { response ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        writerName = response.authorNickname
                    )
                }
                _sideEffect.emit(SopletterWriteSideEffect.ShowSnackbar(message = "메세지 작성을 완료했어요.", type = SopletterSnackbarType.SUCCESS))
                _sideEffect.emit(SopletterWriteSideEffect.NavigateToMain)

            }.onFailure { _ ->
                _uiState.update { it.copy(isLoading = false, isError = true) }

                _sideEffect.emit(SopletterWriteSideEffect.ShowSnackbar(message = "일시적인 오류가 발생했어요.", type = SopletterSnackbarType.FAILURE))
            }
        }
    }

    fun onLimitExceeded() {
        viewModelScope.launch {
            _sideEffect.emit(SopletterWriteSideEffect.ShowSnackbar(message = "공백 포함 350자 이하로만 작성할 수 있어요.", type = SopletterSnackbarType.WARNING))
        }
    }
}