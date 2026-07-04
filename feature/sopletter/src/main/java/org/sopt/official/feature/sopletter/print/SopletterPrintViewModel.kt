package org.sopt.official.feature.sopletter.print

import android.content.Context
import android.graphics.Bitmap
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
import org.sopt.official.domain.sopletter.repository.SopletterRepository
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarType
import org.sopt.official.feature.sopletter.print.manager.PdfHelper
import javax.inject.Inject

@HiltViewModel
class SopletterPrintViewModel @Inject constructor(
    private val sopletterRepository: SopletterRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SopletterPrintUiState())
    val uiState: StateFlow<SopletterPrintUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SopletterPrintSideEffect>()
    val sideEffect: SharedFlow<SopletterPrintSideEffect> = _sideEffect.asSharedFlow()

    init {
        fetchPreviewMessages()
    }


    private fun fetchPreviewMessages() {
        viewModelScope.launch {
            sopletterRepository.getDefaultMessages(
                cursor = null,
                size = 16
            ).onSuccess { response ->
                _uiState.update {
                    it.copy(
                        topicTitle = response.title,
                        totalCount = response.totalCount,
                        previewMemoList = response.messages
                    )
                }
            }
        }
    }

    fun fetchAllAndTriggerCapture() {
        val totalCount = _uiState.value.totalCount

        viewModelScope.launch {
            _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar("이미지 저장 중 ...", SopletterSnackbarType.WARNING))
            _uiState.update { it.copy(isSaving = true) }

            sopletterRepository.getDefaultMessages(
                cursor = null,
                size = if (totalCount > 0) totalCount else 100
            ).onSuccess { response ->
                _uiState.update {
                    it.copy(
                        fullMemoList = response.messages,
                        isCaptureRequested = true
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isSaving = false) }
                _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar("데이터를 가져오는 데 실패했습니다.", SopletterSnackbarType.FAILURE))
            }
        }
    }

    fun processSavePdf(context: Context, bitmap: Bitmap) {
        viewModelScope.launch {
            PdfHelper.saveBitmapAsPdf(
                context = context,
                bitmap = bitmap,
                fileName = "sopletter_${_uiState.value.generation}기"
            ).onSuccess {
                _uiState.update { it.copy(isSaving = false, isCaptureRequested = false, fullMemoList = null) }
                _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar("이미지 저장을 완료했어요.", SopletterSnackbarType.SUCCESS))
                _sideEffect.emit(SopletterPrintSideEffect.NavigateBack)
            }.onFailure {
                _uiState.update { it.copy(isSaving = false, isCaptureRequested = false, fullMemoList = null) }
                _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar("PDF 저장 중 에러 발생.", SopletterSnackbarType.FAILURE))
            }
        }
    }
}