package org.sopt.official.feature.appjamtamp.missionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.feature.appjamtamp.missionlist.model.toUiModel
import org.sopt.official.feature.appjamtamp.missionlist.state.AppjamtampMissionState
import org.sopt.official.feature.appjamtamp.missionlist.state.AppjamtampSideEffect
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppjamtampMissionViewModel @Inject constructor(
    private val appjamtampRepository: AppjamtampRepository,
    private val stampRepository: StampRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AppjamtampMissionState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AppjamtampSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        getReportUrl()
        fetchAppjamMissions()
    }

    fun fetchAppjamMissions() {
        viewModelScope.launch {
            appjamtampRepository.getAppjamtampMissions(
                // Todo : 앱잼정보확인 서버에서 주는 값으로 변경해야함 - appjaminfo
                teamNumber = ""
            ).onSuccess { missions ->
                _state.update { currentState ->
                    currentState.copy(
                        missionList = missions.map { it.toUiModel() }.toImmutableList()
                    )
                }
            }.onFailure(Timber::e)
        }
    }

    private fun getReportUrl() {
        viewModelScope.launch {
            stampRepository.getReportUrl()
                .onSuccess {
                    _state.update { currentState ->
                        currentState.copy(
                            reportUrl = it
                        )
                    }
                }
                .onFailure(Timber::e)
        }
    }

    fun reportButtonClick() {
        viewModelScope.launch {
            _sideEffect.emit(AppjamtampSideEffect.NavigateToWebView)
        }
    }
}