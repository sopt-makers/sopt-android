/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.mission.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.stamp.designsystem.component.toolbar.ToolbarIconType
import org.sopt.official.domain.soptamp.model.Archive
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.domain.soptamp.model.ImageModel
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

data class PostUiState(
    val id: Int = -1,
    val imageUri: ImageModel = ImageModel.Empty,
    val content: String = "",
    val createdAt: String = "",
    val stampId: Int = -1,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error: Throwable? = null,
    val isCompleted: Boolean = false,
    val toolbarIconType: ToolbarIconType = ToolbarIconType.NONE,
    val isDeleteSuccess: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isMe: Boolean = true
) {
    companion object {
        fun from(data: Archive) = PostUiState(
            id = data.missionId,
            imageUri = if (data.images.isEmpty()) ImageModel.Empty else ImageModel.Remote(data.images),
            content = data.contents
        )
    }
}

@OptIn(FlowPreview::class)
@HiltViewModel
class MissionDetailViewModel @Inject constructor(
    private val repository: StampRepository
) : ViewModel() {
    private val uiState = MutableStateFlow(PostUiState())

    private val isMe = uiState.map { it.isMe }
    val isSuccess = uiState.map { it.isSuccess }
    val content = uiState.map { it.content }
    val imageModel = uiState.map { it.imageUri }
    val isSubmitEnabled = combine(content, imageModel, isMe) { content, image, isMe ->
        content.isNotEmpty() && !image.isEmpty() && isMe
    }
    val isCompleted = uiState.map { it.isCompleted }
    val toolbarIconType = uiState.map { it.toolbarIconType }
    val isEditable = toolbarIconType.map {
        it != ToolbarIconType.WRITE
    }
    val createdAt = uiState.map { it.createdAt }
        .filter { it.isNotEmpty() }
    val isDeleteSuccess = uiState.map { it.isDeleteSuccess }
    val isDeleteDialogVisible = uiState.map { it.isDeleteDialogVisible }
    val isError = uiState.map { it.isError }

    private val submitEvent = MutableSharedFlow<Unit>()

    init {
        viewModelScope.launch {
            submitEvent.debounce(500).collect {
                handleSubmit()
            }
        }
    }

    fun initMissionState(
        id: Int,
        isCompleted: Boolean,
        isMe: Boolean,
        nickname: String
    ) {
        viewModelScope.launch {
            uiState.update {
                it.copy(
                    id = id,
                    isError = false,
                    error = null,
                    isLoading = true,
                    isSuccess = false,
                    isMe = isMe
                )
            }
            repository.getMissionContent(id, nickname)
                .onSuccess {
                    val option = if (!isMe) {
                        ToolbarIconType.NONE
                    } else {
                        if (isCompleted) {
                            ToolbarIconType.WRITE
                        } else {
                            ToolbarIconType.NONE
                        }
                    }
                    val result = PostUiState.from(it).copy(
                        stampId = it.id,
                        isCompleted = isCompleted,
                        toolbarIconType = option,
                        createdAt = it.createdAt ?: ""
                    )
                    uiState.update { result }
                }.onFailure { error ->
                    Timber.e(error)
                    if (error is HttpException && error.code() != 400) {
                        uiState.update {
                            it.copy(isLoading = false, isError = true, error = error)
                        }
                    } else {
                        uiState.update {
                            it.copy(isLoading = false, error = error)
                        }
                    }
                }
        }
    }

    fun onChangeContent(content: String) {
        uiState.update {
            it.copy(content = content)
        }
    }

    private fun onChangeToolbarState(toolbarIconType: ToolbarIconType) {
        uiState.update {
            it.copy(toolbarIconType = toolbarIconType)
        }
    }

    fun onPressToolbarIcon() {
        when (uiState.value.toolbarIconType) {
            ToolbarIconType.WRITE -> {
                onChangeToolbarState(ToolbarIconType.DELETE)
            }

            ToolbarIconType.DELETE -> {
                onChangeDeleteDialogVisibility(true)
            }

            ToolbarIconType.NONE -> {}
        }
    }

    fun onChangeImage(imageModel: ImageModel) {
        uiState.update {
            it.copy(imageUri = imageModel)
        }
    }

    fun onChangeDeleteDialogVisibility(value: Boolean) {
        uiState.update {
            it.copy(isDeleteDialogVisible = value)
        }
    }

    fun onSubmit() {
        viewModelScope.launch {
            submitEvent.emit(Unit)
        }
    }

    private suspend fun handleSubmit() {
        viewModelScope.launch {
            val currentState = uiState.value
            val (id, imageUri, content) = currentState
            uiState.update {
                it.copy(isError = false, error = null, isLoading = true)
            }
            if (uiState.value.isCompleted) {
                repository.modifyMission(
                    missionId = id,
                    content = content,
                    imageUri = imageUri
                ).onSuccess {
                    uiState.update {
                        it.copy(isLoading = false, isSuccess = true)
                    }
                }.onFailure { error ->
                    Timber.e(error)
                    uiState.update {
                        it.copy(isLoading = false, isError = true, error = error, isSuccess = false)
                    }
                }
            } else {
                repository.completeMission(
                    missionId = id,
                    content = content,
                    imageUri = imageUri
                ).onSuccess {
                    uiState.update {
                        it.copy(isLoading = false, isSuccess = true)
                    }
                }.onFailure { error ->
                    Timber.e(error)
                    uiState.update {
                        it.copy(isLoading = false, isError = true, error = error, isSuccess = false)
                    }
                }
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            val currentState = uiState.value
            val (id) = currentState
            uiState.update {
                it.copy(isError = false, error = null, isLoading = true)
            }
            repository.deleteMission(id)
                .onSuccess {
                    uiState.update {
                        it.copy(isLoading = false, isDeleteSuccess = true)
                    }
                }.onFailure { error ->
                    Timber.e(error)
                    uiState.update {
                        it.copy(isLoading = false, isError = true, error = error)
                    }
                }
        }
    }

    fun onPressNetworkErrorDialog() {
        uiState.update {
            it.copy(isError = false, error = null)
        }
    }
}
