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
import timber.log.Timber
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

    private var pdfWriter: PdfHelper.ProgressiveWriter? = null

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
                val safeTopicTitle = response.title.toSafeFileName()
                pdfWriter = PdfHelper.ProgressiveWriter("sopletter_$safeTopicTitle")
                _uiState.update { it.copy(fullMemoList = response.messages, isCaptureRequested = true) }
            }.onFailure {
                onCaptureFailed("이미지 저장에 실패했어요.")
            }
        }
    }

    fun addPageToPdf(bitmap: Bitmap) {
        pdfWriter?.addPageAndRecycle(bitmap)
    }

    fun processSavePdf(context: Context) {
        _uiState.update { it.copy(isCaptureRequested = false) }

        viewModelScope.launch {
            try {
                val writer = pdfWriter ?: error("PdfWriter가 초기화되지 않았습니다.")
                writer.saveAndClose(context).onSuccess {
                    _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar("이미지 저장을 완료했어요.", SopletterSnackbarType.SUCCESS))
                    _sideEffect.emit(SopletterPrintSideEffect.NavigateBack)
                }.onFailure { e ->
                    Timber.e(e, "PDF 변환 및 파일 저장 실패")
                    onCaptureFailed("이미지 저장에 실패했어요.")
                }
            } catch (e: Throwable) {
                Timber.e(e, "PDF 처리 중 에러 발생")
                onCaptureFailed("이미지 저장에 실패했어요.")
            } finally {
                pdfWriter = null
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
        pdfWriter?.close()
        pdfWriter = null
        _uiState.update { it.copy(isSaving = false, isCaptureRequested = false, fullMemoList = null) }
        viewModelScope.launch {
            _sideEffect.emit(SopletterPrintSideEffect.ShowSnackbar(message, SopletterSnackbarType.FAILURE))
            _sideEffect.emit(SopletterPrintSideEffect.NavigateBack)
        }
    }
}