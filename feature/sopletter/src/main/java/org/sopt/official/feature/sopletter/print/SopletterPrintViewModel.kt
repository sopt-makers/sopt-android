package org.sopt.official.feature.sopletter.print

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.sopletter.model.SopletterMessages
import org.sopt.official.domain.sopletter.repository.SopletterRepository
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarType
import org.sopt.official.feature.sopletter.common.util.toSafeFileName
import org.sopt.official.feature.sopletter.print.manager.PdfHelper
import org.sopt.official.feature.sopletter.print.model.SopletterPrintSideEffect
import org.sopt.official.feature.sopletter.print.model.SopletterPrintUiState
import org.sopt.official.feature.sopletter.print.navigation.SopletterPrint
import javax.inject.Inject

@HiltViewModel
class SopletterPrintViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sopletterRepository: SopletterRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SopletterPrintUiState())
    val uiState: StateFlow<SopletterPrintUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SopletterPrintSideEffect>()
    val sideEffect: SharedFlow<SopletterPrintSideEffect> = _sideEffect.asSharedFlow()

    private val currentTopicId: Long? = savedStateHandle.toRoute<SopletterPrint>().topicId

    // TODO: uiState.generation이 기본값 0으로 고정되어 있고 갱신 로직이 없음.
    //  실제 기수 정보를 서버에서 받아와 반영하는 로직 추가 필요.
    init {
        fetchPreviewMessages()
    }

    private suspend fun fetchMessages(size: Int): Result<SopletterMessages> {
        val topicId = currentTopicId
        return if (topicId != null) {
            sopletterRepository.getTopicMessages(topicId = topicId, cursor = null, size = size)
        } else {
            sopletterRepository.getDefaultMessages(cursor = null, size = size)
        }
    }

    private fun fetchPreviewMessages() {
        viewModelScope.launch {
            fetchMessages(size = 16).onSuccess { response ->
                _uiState.update { it.copy(topicTitle = response.title, totalCount = response.totalCount, previewMemoList = response.messages) }
            }.onFailure {
                _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar("일시적인 오류가 발생했어요.", SopletterSnackbarType.FAILURE))
            }
        }
    }

    fun fetchAllAndTriggerCapture() {
        if (_uiState.value.isSaving) return

        val totalCount = _uiState.value.totalCount

        viewModelScope.launch {
            _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar("이미지 저장 중 ...", SopletterSnackbarType.WARNING))
            _uiState.update { it.copy(isSaving = true) }

            fetchMessages(size = if (totalCount > 0) totalCount else 100).onSuccess { response ->
                _uiState.update { it.copy(fullMemoList = response.messages, isCaptureRequested = true) }
            }.onFailure {
                onCaptureFailed("이미지 저장에 실패했어요.")
            }
        }
    }

    fun processSavePdf(context: Context, bitmaps: List<Bitmap>) {
         _uiState.update { it.copy(isCaptureRequested = false) }

        viewModelScope.launch {
            try {
                val safeTopicTitle = _uiState.value.topicTitle.toSafeFileName()

                PdfHelper.saveBitmapsAsPdf(
                    context = context,
                    bitmaps = bitmaps,
                    fileName = "sopletter_$safeTopicTitle",
                ).onSuccess {
                    _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar("이미지 저장을 완료했어요.", SopletterSnackbarType.SUCCESS))
                    _sideEffect.emit(SopletterPrintSideEffect.NavigateBack)
                }.onFailure { e ->
                    e.printStackTrace()
                    onCaptureFailed("이미지 저장에 실패했어요.")
                }
            } finally {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        fullMemoList = null
                    )
                }
            }
        }
    }

    fun onCaptureFailed(message: String = "이미지 캡처 중 에러가 발생했습니다.") {
        _uiState.update { it.copy(isSaving = false, isCaptureRequested = false, fullMemoList = null) }
        viewModelScope.launch {
            _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar(message, SopletterSnackbarType.FAILURE))
            _sideEffect.emit(SopletterPrintSideEffect.NavigateBack)
        }
    }
}
