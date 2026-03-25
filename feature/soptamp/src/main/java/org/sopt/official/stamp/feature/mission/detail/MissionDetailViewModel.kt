/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import org.sopt.official.domain.mypage.repository.UserRepository
import org.sopt.official.domain.soptamp.model.ImageModel
import org.sopt.official.domain.soptamp.model.Stamp
import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.stamp.designsystem.component.toolbar.ToolbarIconType
import org.sopt.official.stamp.feature.mission.detail.model.StampClapUiModel
import org.sopt.official.stamp.feature.mission.detail.model.toUiModel
import org.sopt.official.stamp.feature.mission.detail.state.MissionDetailUiState
import org.sopt.official.stamp.feature.mission.detail.type.MissionDetailModeType
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
internal class MissionDetailViewModel @Inject constructor(
    private val stampRepository: StampRepository,
    private val imageUploaderRepository: ImageUploaderRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val uiState = MutableStateFlow(MissionDetailUiState())
    val missionDetailUiState = uiState.asStateFlow()
    private val clapEvent = MutableSharedFlow<Unit>()
    private var debounceJob: Job? = null
    private var isPosting = false

    val isSubmitEnabled = uiState.map { state ->
        val commonGuard =
            state.isMe &&
                !state.isLoading &&
                !state.isSuccess &&
                isFilledRequiredFields(state)

        if (!commonGuard) return@map false

        when (state.mode) {
            MissionDetailModeType.WRITE -> true
            MissionDetailModeType.EDIT -> isRequiredFieldsChanged(state)
            MissionDetailModeType.READ_ONLY -> false
        }
    }
    val isBadgeVisible = uiState.map { it.isBadgeVisible }

    val appliedCount = uiState.map { it.appliedCount } // 앰플(이번 요청으로 실제 반영된 증가량)

    val totalClapCount = uiState.map { it.totalClapCount }
    val viewCount = uiState.map { it.viewCount }
    val myClapCount = uiState.map { it.myClapCount }
    val clappers = uiState.map { it.clappers }

    private val _myNickname = MutableStateFlow("")
    val myNickname = _myNickname.asStateFlow()

    init {
        observeDebouncedClaps()
    }

    // 딥링크로 미션 상세 뷰에 접속한 경우 "나"의 닉네임을 얻음 (앰플리튜드 삽입 목적)
    fun getMyName() {
        viewModelScope.launch {
            userRepository.getUserInfo()
                .onSuccess {
                    _myNickname.value = it.nickname
                }
                .onFailure(Timber::e)
        }
    }

    fun initMissionState(
        id: Int,
        isCompleted: Boolean,
        isMe: Boolean,
        nickname: String,
    ) {
        viewModelScope.launch {
            uiState.update {
                it.copy(
                    id = id,
                    isError = false,
                    error = null,
                    isLoading = true,
                    isSuccess = false,
                    isMe = isMe,
                )
            }
            if (isCompleted) {
                stampRepository.getMissionContent(id, nickname)
                    .onSuccess {
                        val mine = it.mine ?: isMe
                        val initialMode = MissionDetailModeType.READ_ONLY
                        val option = if (!mine) ToolbarIconType.NONE else ToolbarIconType.WRITE
                        val remoteImage = ImageModel.Remote(url = it.images)
                        val result = MissionDetailUiState.from(it).copy(
                            stampId = it.id,
                            imageUri = remoteImage,
                            isCompleted = true,
                            mode = initialMode,
                            toolbarIconType = option,
                            totalClapCount = it.clapCount,
                            viewCount = it.viewCount,
                            myClapCount = it.myClapCount,
                            isMe = mine,
                            initSnapshotImageUri = remoteImage,
                            initSnapshotContent = it.contents,
                            initSnapshotDate = it.activityDate,
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
            } else {
                uiState.update {
                    it.copy(
                        imageUri = ImageModel.Empty,
                        content = "",
                        date = "",
                        isLoading = false,
                        isCompleted = false,
                        mode = MissionDetailModeType.WRITE,
                        toolbarIconType = ToolbarIconType.NONE,
                        initSnapshotImageUri = ImageModel.Empty,
                        initSnapshotContent = "",
                        initSnapshotDate = "",
                    )
                }
            }
        }
    }

    fun onChangeContent(content: String) {
        uiState.update {
            it.copy(content = content)
        }
    }

    fun onPressToolbarIcon() {
        val state = uiState.value
        if (!state.isMe) return

        when (state.mode) {
            MissionDetailModeType.READ_ONLY -> {
                uiState.update {
                    it.copy(
                        mode = MissionDetailModeType.EDIT,
                        toolbarIconType = ToolbarIconType.DELETE,
                    )
                }
            }

            MissionDetailModeType.EDIT -> {
                onChangeDeleteDialogVisibility(true)
            }

            MissionDetailModeType.WRITE -> Unit
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

    fun onChangeDate(value: String) {
        uiState.update {
            it.copy(date = value)
        }
    }

    fun onChangeDatePickerBottomSheetOpened(value: Boolean) {
        uiState.update {
            it.copy(isBottomSheetOpened = value)
        }
    }

    fun onSubmit() {
        val state = uiState.value
        if (state.isLoading || state.isSuccess) return
        if (!isFilledRequiredFields(state)) return
        if (state.mode == MissionDetailModeType.READ_ONLY) return

        viewModelScope.launch {
            handleSubmit()
        }
    }

    private suspend fun handleSubmit() {
        val currentState = uiState.value
        if (currentState.isLoading || currentState.isSuccess) return
        if (!isFilledRequiredFields(currentState)) return
        if (currentState.mode == MissionDetailModeType.READ_ONLY) return

        val (id, imageUri, content, date) = currentState
        uiState.update {
            it.copy(isError = false, error = null, isLoading = true)
        }

        val image =
            when (imageUri) {
                ImageModel.Empty -> {
                    "ERROR"
                }

                is ImageModel.Local -> {
                    imageUri.uri[0]
                }

                is ImageModel.Remote -> {
                    imageUri.url[0]
                }
            }

        if (imageUri is ImageModel.Remote) {
            modifyMission(id, image, content, date)
        } else {
            imageUploaderRepository.getImageUploadURL().onSuccess { s3Url ->
                val preSignedURL = s3Url.preSignedURL
                val imageURL = s3Url.imageURL

                runCatching {
                    imageUploaderRepository.uploadImage(
                        preSignedURL = preSignedURL,
                        imageUri = image,
                    )
                }.onFailure {
                    Timber.e(it.toString())
                }

                when (uiState.value.mode) {
                    MissionDetailModeType.WRITE -> completeMission(id, imageURL, content, date)
                    MissionDetailModeType.EDIT -> modifyMission(id, imageURL, content, date)
                    MissionDetailModeType.READ_ONLY -> Unit
                }
            }.onFailure { error ->
                Timber.e(error)
                uiState.update {
                    it.copy(isLoading = false, isError = true, error = error, isSuccess = false)
                }
            }
        }
    }

    private suspend fun modifyMission(
        id: Int,
        image: String,
        content: String,
        date: String,
    ) {
        stampRepository.modifyMission(
            Stamp(
                missionId = id,
                image = image,
                contents = content,
                activityDate = date,
            ),
        ).onSuccess {
            uiState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = false,
                    mode = MissionDetailModeType.READ_ONLY,
                    toolbarIconType = ToolbarIconType.WRITE,
                    isShowEditSnackBar = true,
                    imageUri = ImageModel.Remote(url = listOf(image)),
                    content = content,
                    date = date,
                    initSnapshotImageUri = ImageModel.Remote(url = listOf(image)),
                    initSnapshotContent = content,
                    initSnapshotDate = date,
                )
            }
        }.onFailure { error ->
            Timber.e(error)
            uiState.update {
                it.copy(isLoading = false, isError = true, error = error, isSuccess = false)
            }
        }
    }

    private suspend fun completeMission(
        id: Int,
        image: String,
        content: String,
        date: String,
    ) {
        stampRepository.completeMission(
            Stamp(
                missionId = id,
                image = image,
                contents = content,
                activityDate = date,
            ),
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

    fun onDelete() {
        viewModelScope.launch {
            uiState.update {
                it.copy(isError = false, error = null, isLoading = true)
            }
            stampRepository.deleteMission(uiState.value.stampId)
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

    fun onHideEditSnackBar() {
        uiState.update {
            it.copy(isShowEditSnackBar = false)
        }
    }

    private fun observeDebouncedClaps() {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            clapEvent
                .debounce(2000L)
                .collect {
                    val currentMyClapCount = uiState.value.myClapCount

                    if (currentMyClapCount != null && currentMyClapCount > 0) {
                        postClapDataIfNeeded()
                    }
                }
        }
    }

    fun onPressClap() {
        val stateAfterUpdate = uiState.updateAndGet { state ->
            if (state.myClapCount != null && state.myClapCount < 50) {
                state.copy(
                    totalClapCount = state.totalClapCount + 1,
                    myClapCount = state.myClapCount + 1,
                    unSyncedClapCount = state.unSyncedClapCount + 1
                )
            } else state
        }

        // 값 변경이 될 경우에만 debounce
        if (stateAfterUpdate.unSyncedClapCount > 0) {
            viewModelScope.launch {
                clapEvent.emit(Unit)
            }
        }

        viewModelScope.launch {
            uiState.update {
                it.copy(
                    isBadgeVisible = true
                )
            }
            delay(500L)
            uiState.update {
                it.copy(
                    isBadgeVisible = false
                )
            }
        }
    }


    fun flushClapDataOnExit() {
        debounceJob?.cancel()
        postClapDataIfNeeded()
    }

    private fun postClapDataIfNeeded() {
        val state = uiState.value
        if (isPosting || state.unSyncedClapCount <= 0) return

        isPosting = true
        val stampId = state.stampId
        val clapToSend = state.unSyncedClapCount

        viewModelScope.launch {
            val clapData = StampClapUiModel(clapCount = clapToSend)
            stampRepository.clapStamp(stampId, clapData.toDomain())
                .onSuccess { clapResult ->
                    uiState.update {
                        it.copy(
                            totalClapCount = clapResult.totalClapCount,
                            appliedCount = clapResult.appliedCount,
                            unSyncedClapCount = 0
                        )
                    }
                }
                .onFailure(Timber::e)

            isPosting = false
        }
    }

    fun getStampClappers(stampId: Int) {
        viewModelScope.launch {
            stampRepository.getClappers(stampId)
                .onSuccess { result ->
                    uiState.update { currentState ->
                        currentState.copy(
                            clappers = result.clappers.map { it.toUiModel() }.toImmutableList()
                        )
                    }
                }
                .onFailure(Timber::e)
        }
    }

    private fun isFilledRequiredFields(state: MissionDetailUiState): Boolean {
        return state.content.isNotBlank() &&
            state.date.isNotBlank() &&
            !state.imageUri.isEmpty()
    }

    private fun isRequiredFieldsChanged(state: MissionDetailUiState): Boolean {
        return state.content != state.initSnapshotContent ||
            state.date != state.initSnapshotDate ||
            state.imageUri != state.initSnapshotImageUri
    }

}
