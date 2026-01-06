package org.sopt.official.feature.appjamtamp.missiondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.feature.appjamtamp.missiondetail.model.DetailViewType
import org.sopt.official.feature.appjamtamp.model.ImageModel

@OptIn(FlowPreview::class)
@HiltViewModel
internal class MissionDetailViewModel @Inject constructor(

) : ViewModel() {
    private val _missionDetailState = MutableStateFlow<MissionDetailState>(MissionDetailState())
    val missionDetailState: StateFlow<MissionDetailState>
        get() = _missionDetailState.asStateFlow()

    private val submitEvent = MutableSharedFlow<Unit>()

    init {
        viewModelScope.launch {
            submitEvent.debounce(500).collect {
                handleSubmit()
            }

            _missionDetailState
                .map { it.unSyncedClapCount }
                .filter { it > 0 }
                .debounce(2_000)
                .collect {

                }
        }
    }

    fun onSubmit() {
        viewModelScope.launch {
            submitEvent.emit(Unit)
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

    private suspend fun handleSubmit() {
        val currentState = _missionDetailState.value

        val image = when (val imageModel = currentState.imageModel) {
            is ImageModel.Empty -> {
                "ERROR"
            }

            is ImageModel.Local -> {
                imageModel.uri[0]
            }

            is ImageModel.Remote -> {
                imageModel.url[0]
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
