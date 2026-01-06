package org.sopt.official.feature.appjamtamp.missiondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository
import org.sopt.official.feature.appjamtamp.missiondetail.model.DetailViewType
import org.sopt.official.feature.appjamtamp.missiondetail.navigation.AppjamtampMissionDetail
import org.sopt.official.feature.appjamtamp.model.ImageModel
import org.sopt.official.feature.appjamtamp.model.Mission
import org.sopt.official.feature.appjamtamp.model.User
import timber.log.Timber

@OptIn(FlowPreview::class)
@HiltViewModel
internal class MissionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appjamtampRepository: AppjamtampRepository,
    private val imageUploaderRepository: ImageUploaderRepository,
) : ViewModel() {
    private val route: AppjamtampMissionDetail = savedStateHandle.toRoute<AppjamtampMissionDetail>()

    private val _missionDetailState = MutableStateFlow<MissionDetailState>(MissionDetailState())
    val missionDetailState: StateFlow<MissionDetailState>
        get() = _missionDetailState.asStateFlow()

    init {
        viewModelScope.launch {
            _missionDetailState
                .map { it.unSyncedClapCount }
                .filter { it > 0 }
                .debounce(2_000)
                .collect {

                }
        }

        initMissionState()
    }

    private fun initMissionState() {
        if (route.ownerName == null) {
            _missionDetailState.update {
                it.copy(
                    isLoading = false,
                    viewType = DetailViewType.WRITE,
                    mission = Mission(
                        id = route.missionId,
                        title = route.title,
                        level = MissionLevel.of(route.missionLevel),
                        isCompleted = false
                    )
                )
            }
        } else {
            getMissionContent(missionId = route.missionId, name = route.ownerName)
        }
    }

    private fun getMissionContent(missionId: Int, name: String) {
        viewModelScope.launch {
            appjamtampRepository.getAppjamtampStamp(missionId = missionId, nickname = name)
                .onSuccess { stamp ->
                    val viewType = if (stamp.isMine) DetailViewType.COMPLETE else DetailViewType.READ_ONLY
                    _missionDetailState.update {
                        it.copy(
                            isLoading = false,
                            viewType = viewType,
                            mission = Mission(
                                id = route.missionId,
                                title = route.title,
                                level = MissionLevel.of(route.missionLevel),
                                isCompleted = true
                            ),
                            imageModel = ImageModel.Remote(stamp.images),
                            date = stamp.activityDate,
                            content = stamp.contents,
                            header = if (stamp.isMine) "내 미션" else stamp.teamName,
                            writer = User(
                                name = stamp.ownerNickname,
                                profileImage = stamp.ownerProfileImage.orEmpty()
                            ),
                            clapCount = stamp.clapCount,
                            myClapCount = stamp.myClapCount,
                            viewCount = stamp.viewCount
                        )
                    }

                }.onFailure { e ->
                    _missionDetailState.update {
                        it.copy(
                            isLoading = false,
                            isFailed = true
                        )
                    }
                    Timber.e(e)
                }
        }
    }

    fun onClap() {
        if (_missionDetailState.value.myClapCount >= 50) return

        _missionDetailState.update {
            it.copy(
                clapCount = it.clapCount + 1,
                myClapCount = it.myClapCount + 1,
                unSyncedClapCount = it.unSyncedClapCount + 1
            )
        }
    }

    fun handleSubmit() {
        _missionDetailState.update {
            it.copy(isLoading = true)
        }

        if (_missionDetailState.value.viewType == DetailViewType.WRITE) {
            submitMission()
        } else {
            modifyMission()
        }
    }

    private fun submitMission() {
        viewModelScope.launch {
            uploadImage()

            with(_missionDetailState.value) {
                if (imageModel is ImageModel.Remote) {
                    appjamtampRepository.postAppjamtampStamp(
                        missionId = mission.id,
                        image = imageModel.url[0],
                        contents = content,
                        activityDate = date
                    ).onSuccess {
                        _missionDetailState.update {
                            it.copy(
                                isLoading = false,
                                viewType = DetailViewType.COMPLETE,
                                header = "내 미션"
                            )
                        }
                    }.onFailure { e ->
                        _missionDetailState.update {
                            it.copy(
                                isLoading = false,
                                isFailed = true
                            )
                        }
                        Timber.e(e)
                    }
                }
            }
        }
    }

    private fun modifyMission() {

    }

    private suspend fun uploadImage() {
        val image = (_missionDetailState.value.imageModel as? ImageModel.Local)?.uri ?: return

        imageUploaderRepository.getImageUploadURL()
            .onSuccess { s3Url ->
                val presignedURL = s3Url.preSignedURL
                val imageUrl = s3Url.imageURL

                suspendRunCatching {
                    imageUploaderRepository.uploadImage(
                        preSignedURL = presignedURL,
                        imageUri = image[0]
                    )
                }.onSuccess {
                    _missionDetailState.update {
                        it.copy(
                            imageModel = ImageModel.Remote(listOf(imageUrl))
                        )
                    }
                }.onFailure(Timber::e)

            }.onFailure {
                _missionDetailState.update {
                    it.copy(
                        isLoading = false,
                        isFailed = true
                    )
                }
            }
    }

    fun updateViewType() {
        _missionDetailState.update {
            it.copy(
                viewType = when (val viewType = _missionDetailState.value.viewType) {
                    DetailViewType.COMPLETE -> DetailViewType.EDIT
                    DetailViewType.EDIT -> DetailViewType.COMPLETE
                    else -> viewType
                }
            )
        }
    }

    fun updateImageModel(imageModel: ImageModel) {
        _missionDetailState.update {
            it.copy(imageModel = imageModel)
        }
    }

    fun updateDate(value: String) {
        _missionDetailState.update {
            it.copy(date = value)
        }
    }

    fun updateContent(value: String) {
        _missionDetailState.update {
            it.copy(content = value)
        }
    }
}
