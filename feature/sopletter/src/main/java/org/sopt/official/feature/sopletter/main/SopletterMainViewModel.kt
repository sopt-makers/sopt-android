/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.sopletter.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.domain.sopletter.repository.SopletterRepository
import org.sopt.official.domain.sopletter.onboarding.usecase.SopletterProfileUseCase
import org.sopt.official.feature.sopletter.common.util.isSopletterNotFound
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarType
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarVisuals
import org.sopt.official.feature.sopletter.common.util.characterCount
import org.sopt.official.feature.sopletter.main.contract.SOPLETTER_MEMO_MAX_LENGTH
import org.sopt.official.feature.sopletter.main.contract.SopletterMainSideEffect
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.feature.sopletter.main.contract.toMemoDetailDialogState
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import org.sopt.official.feature.sopletter.main.navigation.SopletterMain
import javax.inject.Inject

@HiltViewModel
class SopletterMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sopletterRepository: SopletterRepository,
    private val profileUseCase: SopletterProfileUseCase,
) : ViewModel(), SopletterMemoDetailDialogContract.Actions {
    val routeTopicId: Long = savedStateHandle.toRoute<SopletterMain>().topicId
    private val pushMessageId: Long? = savedStateHandle.toRoute<SopletterMain>().messageId

    private val _uiState = MutableStateFlow(SopletterMainUiState())
    val uiState: StateFlow<SopletterMainUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SopletterMainSideEffect>()
    val sideEffect: SharedFlow<SopletterMainSideEffect> = _sideEffect.asSharedFlow()
    private val pendingRetries = mutableSetOf<RetryTarget>()

    private sealed interface RetryTarget {
        data class Messages(val isLoadMore: Boolean) : RetryTarget
        data class MessageDetail(val messageId: Long) : RetryTarget
    }

    init {
        fetchMessages()
        openPushMessageDetail()
    }

    // 좋아요 푸시로 진입한 경우 목록 로드를 기다리지 않고 상세 모달을 바로 연다.
    private fun openPushMessageDetail() {
        val messageId = pushMessageId ?: return
        fetchMemoDetail(messageId = messageId)
    }

    // ---------------- Main screen ----------------

    fun refreshMainContent() {
        fetchMessages()
    }

    fun fetchMessages(isLoadMore: Boolean = false) = viewModelScope.launch {
        val currentState = _uiState.value
        if (currentState.isLoading) return@launch

        val cursor = if (isLoadMore) {
            if (!currentState.hasNext) return@launch
            currentState.nextCursor ?: return@launch
        } else {
            null
        }

        _uiState.update { state ->
            state.copy(
                isLoading = true,
                isMessageRefreshing = !isLoadMore && currentState.isInitialized,
                isPaging = isLoadMore,
                isShowErrorDialog = pendingRetries.isNotEmpty(),
            )
        }

        sopletterRepository.getTopicMessages(
            topicId = routeTopicId,
            cursor = cursor,
        )
            .onSuccess { response ->
                pendingRetries.remove(RetryTarget.Messages(isLoadMore))
                _uiState.update { state ->
                    state.copy(
                        topicId = response.topicId,
                        topicTitle = response.title,
                        totalCount = response.totalCount,
                        nextCursor = response.nextCursor,
                        hasNext = response.hasNext,
                        memoList = if (isLoadMore) {
                            (state.memoList + response.messages)
                                .distinctBy(SopletterMessage::messageId)
                                .toPersistentList()
                        } else {
                            response.messages.toPersistentList()
                        },
                        isInitialized = true,
                        isLoading = false,
                        isMessageRefreshing = false,
                        isPaging = false,
                        isShowErrorDialog = pendingRetries.isNotEmpty(),
                    )
                }
            }
            .onFailure { error ->
                val onboardingStatus = resolveOnboardingAfterNotFound(error)
                val onboardingRequired = _uiState.value.isShowOnboardingDialog
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isMessageRefreshing = false,
                        isPaging = false,
                        isShowErrorDialog = onboardingStatus != false && !onboardingRequired,
                    )
                }
                if (onboardingStatus != false && !onboardingRequired) {
                    pendingRetries.add(RetryTarget.Messages(isLoadMore))
                }
            }
    }

    fun openReportForm() = viewModelScope.launch {
        _uiState.value.reportFormUrl?.let { url ->
            _sideEffect.emit(SopletterMainSideEffect.NavigateToReportForm(url))
            return@launch
        }

        if (_uiState.value.isLoading) return@launch

        _uiState.update { state ->
            state.copy(
                isLoading = true,
                isShowErrorDialog = pendingRetries.isNotEmpty(),
            )
        }

        sopletterRepository.getReportFormUrl()
            .onSuccess { url ->
                _uiState.update { state ->
                    state.copy(
                        reportFormUrl = url,
                        isLoading = false,
                    )
                }
                _sideEffect.emit(SopletterMainSideEffect.NavigateToReportForm(url))
            }
            .onFailure { error ->
                val onboardingStatus = resolveOnboardingAfterNotFound(error)
                _uiState.update { state ->
                    state.copy(isLoading = false)
                }
                if (onboardingStatus != false) {
                    _sideEffect.emit(
                        SopletterMainSideEffect.ShowSnackbar(
                            SopletterSnackbarVisuals(
                                message = "일시적인 오류가 발생했어요.",
                                type = SopletterSnackbarType.FAILURE,
                            ),
                        ),
                    )
                }
            }
    }

    fun retryError() {
        val retryTargets = pendingRetries.toList()
        pendingRetries.clear()
        _uiState.update { state ->
            state.copy(isShowErrorDialog = false)
        }
        retryTargets.forEach { retryTarget ->
            when (retryTarget) {
                is RetryTarget.Messages -> fetchMessages(isLoadMore = retryTarget.isLoadMore)
                is RetryTarget.MessageDetail -> fetchMemoDetail(retryTarget.messageId)
            }
        }
    }

    // ---------------- Memo detail dialog ----------------

    fun fetchMemoDetail(messageId: Long) = viewModelScope.launch {
        val topicId = routeTopicId

        sopletterRepository.getMessageDetail(
            topicId = topicId,
            messageId = messageId,
        ).onSuccess { detail ->
            pendingRetries.remove(RetryTarget.MessageDetail(messageId))
            _uiState.update { state ->
                state.copy(
                    topicId = topicId,
                    selectedMemoDetail = detail.toMemoDetailDialogState(),
                    isShowErrorDialog = pendingRetries.isNotEmpty(),
                )
            }
        }.onFailure { error ->
            val onboardingStatus = resolveOnboardingAfterNotFound(error)
            if (onboardingStatus == true) {
                _sideEffect.emit(
                    SopletterMainSideEffect.ShowSnackbar(
                        visuals = SopletterSnackbarVisuals(
                            message = "삭제되었거나 볼 수 없는 메시지예요",
                            type = SopletterSnackbarType.WARNING,
                        ),
                    ),
                )
            } else if (onboardingStatus != false && !_uiState.value.isShowOnboardingDialog) {
                pendingRetries.add(RetryTarget.MessageDetail(messageId))
                _uiState.update { state ->
                    state.copy(isShowErrorDialog = true)
                }
            }
        }
    }

    override fun onLikeClick() {
        val currentState = _uiState.value
        val selectedMemoDetail = currentState.selectedMemoDetail ?: return
        val topicId = currentState.topicId

        if (selectedMemoDetail.isMine) {
            viewModelScope.launch {
                _sideEffect.emit(
                    SopletterMainSideEffect.ShowSnackbar(
                        visuals = SopletterSnackbarVisuals(
                            message = "내가 작성한 솝레터에는 좋아요를 누를 수 없어요.",
                            type = SopletterSnackbarType.WARNING,
                        ),
                    ),
                )
            }
            return
        }
        if (topicId <= 0L) return

        viewModelScope.launch {
            updateSelectedMemoLikeState(
                memoId = selectedMemoDetail.memoId,
                isLiked = !selectedMemoDetail.isLiked,
                likeCount = if (selectedMemoDetail.isLiked) {
                    selectedMemoDetail.likeCount - 1
                } else {
                    selectedMemoDetail.likeCount + 1
                },
            )

            val result = if (selectedMemoDetail.isLiked) {
                sopletterRepository.deleteMessageLike(
                    topicId = topicId,
                    messageId = selectedMemoDetail.memoId,
                )
            } else {
                sopletterRepository.addMessageLike(
                    topicId = topicId,
                    messageId = selectedMemoDetail.memoId,
                )
            }

            result.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isShowErrorDialog = pendingRetries.isNotEmpty(),
                    )
                }
            }.onFailure { error ->
                updateSelectedMemoLikeState(
                    memoId = selectedMemoDetail.memoId,
                    isLiked = selectedMemoDetail.isLiked,
                    likeCount = selectedMemoDetail.likeCount,
                )
                if (resolveOnboardingAfterNotFound(error) != false) {
                    showFailureSnackbar()
                }
            }
        }
    }

    override fun onEditClick() {
        _uiState.update { state ->
            val currentDetail = state.selectedMemoDetail ?: return@update state

            state.copy(
                selectedMemoDetail = currentDetail.copy(isEditing = true),
            )
        }
    }

    override fun onEditCancelClick() {
        _uiState.update { state ->
            val currentDetail = state.selectedMemoDetail ?: return@update state

            state.copy(
                selectedMemoDetail = currentDetail.copy(isEditing = false),
            )
        }
    }

    override fun showMemoLengthWarning() {
        viewModelScope.launch {
            _sideEffect.emit(
                SopletterMainSideEffect.ShowSnackbar(
                    visuals = SopletterSnackbarVisuals(
                        message = "공백 포함 350자 이하로만 작성할 수 있어요.",
                        type = SopletterSnackbarType.WARNING,
                    ),
                ),
            )
        }
    }

    override fun onEditCompleteClick(content: String) {
        if (content.isBlank()) return
        if (content.characterCount() > SOPLETTER_MEMO_MAX_LENGTH) {
            showMemoLengthWarning()
            return
        }

        val currentState = _uiState.value
        val selectedMemoDetail = currentState.selectedMemoDetail ?: return
        val topicId = currentState.topicId
        if (topicId <= 0L) return

        viewModelScope.launch {
            sopletterRepository.updateMessage(
                topicId = topicId,
                messageId = selectedMemoDetail.memoId,
                content = content,
            ).onSuccess { updatedMessage ->
                _uiState.update { state ->
                    val currentDetail = state.selectedMemoDetail

                    state.copy(
                        memoList = state.memoList.map { message ->
                            if (message.messageId == updatedMessage.messageId) {
                                message.copy(previewContent = updatedMessage.content)
                            } else {
                                message
                            }
                        }.toPersistentList(),
                        selectedMemoDetail = if (currentDetail?.memoId == updatedMessage.messageId) {
                            updatedMessage.toMemoDetailDialogState()
                                .copy(memoColor = currentDetail.memoColor)
                        } else {
                            currentDetail
                        },
                        isShowErrorDialog = pendingRetries.isNotEmpty(),
                    )
                }

                _sideEffect.emit(
                    SopletterMainSideEffect.ShowSnackbar(
                        visuals = SopletterSnackbarVisuals(
                            message = "메시지 수정을 완료했어요.",
                            type = SopletterSnackbarType.SUCCESS,
                        ),
                    ),
                )
            }.onFailure { error ->
                val onboardingStatus = resolveOnboardingAfterNotFound(error)
                _uiState.update { state ->
                    state.copy(isShowErrorDialog = pendingRetries.isNotEmpty())
                }
                if (onboardingStatus != false) showFailureSnackbar()
            }
        }
    }

    override fun onDeleteClick() {
        _uiState.update { state ->
            state.copy(isDeleteDialogVisible = true)
        }
    }

    override fun onDeleteDialogDismissClick() {
        _uiState.update { state ->
            state.copy(isDeleteDialogVisible = false)
        }
    }

    override fun onDeleteConfirmClick() {
        val currentState = _uiState.value
        val selectedMemoDetail = currentState.selectedMemoDetail ?: return
        val topicId = currentState.topicId
        if (topicId <= 0L) return

        viewModelScope.launch {
            sopletterRepository.deleteMessage(
                topicId = topicId,
                messageId = selectedMemoDetail.memoId,
            ).onSuccess {
                _uiState.update { state ->
                    state.copy(
                        totalCount = (state.totalCount - 1).coerceAtLeast(0),
                        memoList = state.memoList
                            .filterNot { message -> message.messageId == selectedMemoDetail.memoId }
                            .toPersistentList(),
                        selectedMemoDetail = null,
                        isDeleteDialogVisible = false,
                        isShowErrorDialog = pendingRetries.isNotEmpty(),
                    )
                }

                _sideEffect.emit(
                    SopletterMainSideEffect.ShowSnackbar(
                        visuals = SopletterSnackbarVisuals(
                            message = "메시지 삭제를 완료했어요.",
                            type = SopletterSnackbarType.SUCCESS,
                        ),
                    ),
                )
            }.onFailure { error ->
                val onboardingStatus = resolveOnboardingAfterNotFound(error)
                _uiState.update { state ->
                    state.copy(
                        isDeleteDialogVisible = false,
                        isShowErrorDialog = pendingRetries.isNotEmpty(),
                    )
                }
                if (onboardingStatus != false) showFailureSnackbar()
            }
        }
    }

    override fun onDismissClick() {
        _uiState.update { state ->
            state.copy(
                selectedMemoDetail = null,
                isDeleteDialogVisible = false,
            )
        }
    }

    private suspend fun resolveOnboardingAfterNotFound(error: Throwable): Boolean? {
        if (!error.isSopletterNotFound()) return null

        val profile = profileUseCase.refresh().getOrNull() ?: return null
        if (profile.isOnboarded) return true

        pendingRetries.clear()
        _uiState.update { state ->
            state.copy(
                isShowErrorDialog = pendingRetries.isNotEmpty(),
                isShowOnboardingDialog = true,
            )
        }
        return false
    }

    private suspend fun showFailureSnackbar() {
        _sideEffect.emit(
            SopletterMainSideEffect.ShowSnackbar(
                SopletterSnackbarVisuals(
                    message = "일시적인 오류가 발생했어요.",
                    type = SopletterSnackbarType.FAILURE,
                ),
            ),
        )
    }

    private fun updateSelectedMemoLikeState(
        memoId: Long,
        isLiked: Boolean,
        likeCount: Long,
    ) {
        _uiState.update { state ->
            val currentDetail = state.selectedMemoDetail ?: return@update state
            if (currentDetail.memoId != memoId) return@update state

            state.copy(
                isShowErrorDialog = pendingRetries.isNotEmpty(),
                selectedMemoDetail = currentDetail.copy(
                    isLiked = isLiked,
                    likeCount = likeCount,
                ),
            )
        }
    }
}
