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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import org.sopt.official.feature.sopletter.main.contract.SopletterMainSideEffect
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import javax.inject.Inject

@HiltViewModel
class SopletterMainViewModel @Inject constructor(
    private val sopletterRepository: SopletterRepository,
) : ViewModel(), SopletterMemoDetailDialogContract.Actions {
    private val _uiState = MutableStateFlow(SopletterMainUiState())
    val uiState: StateFlow<SopletterMainUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SopletterMainSideEffect>()
    val sideEffect: SharedFlow<SopletterMainSideEffect> = _sideEffect.asSharedFlow()

    init {
        fetchDefaultMessages()
    }

    fun updateSelectMemoDetail(memo: SopletterMemoDetailDialogContract.State) {
        _uiState.update { state ->
            state.copy(selectedMemoDetail = memo)
        }
    }

    override fun onLikeClick() {
        val selectedMemoDetail = _uiState.value.selectedMemoDetail ?: return

        if (selectedMemoDetail.isMine) {
            viewModelScope.launch {
                _sideEffect.emit(
                    SopletterMainSideEffect.ShowSnackbar("내가 작성한 솝레터에는 좋아요를 누를 수 없어요."),
                )
            }
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

    fun fetchDefaultMessages(isLoadMore: Boolean = false) = viewModelScope.launch {
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
                isShowErrorDialog = false,
            )
        }

        sopletterRepository.getDefaultMessages(cursor = cursor)
            .onSuccess { response ->
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
                        isShowErrorDialog = false,
                    )
                }
            }
            .onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isMessageRefreshing = false,
                        isPaging = false,
                        isShowErrorDialog = true,
                    )
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
                isShowErrorDialog = false,
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
            .onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isShowErrorDialog = true,
                    )
                }
            }
    }

    fun dismissErrorDialog() {
        _uiState.update { state ->
            state.copy(isShowErrorDialog = false)
        }
    }
}
