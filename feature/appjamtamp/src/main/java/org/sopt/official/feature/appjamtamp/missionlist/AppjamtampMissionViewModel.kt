package org.sopt.official.feature.appjamtamp.missionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel
import org.sopt.official.feature.appjamtamp.missionlist.model.toUiModel
import org.sopt.official.feature.appjamtamp.missionlist.state.AppjamtampMissionState
import org.sopt.official.feature.appjamtamp.missionlist.state.AppjamtampSideEffect
import org.sopt.official.feature.appjamtamp.model.MissionFilter
import timber.log.Timber

@HiltViewModel
internal class AppjamtampMissionViewModel @Inject constructor(
    private val appjamtampRepository: AppjamtampRepository,
    private val stampRepository: StampRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AppjamtampMissionState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AppjamtampSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        getReportUrl()
    }

    fun fetchAppjamMissions(isCompleted: Boolean? = null) {
        viewModelScope.launch {
            appjamtampRepository.getAppjamtampMissions(
                isCompleted = isCompleted
            ).onSuccess { missions ->
                _state.update { currentState ->
                    currentState.copy(
                        teamName = missions.teamName,
                        missionList = missions.toUiModel()
                    )
                }
            }.onFailure(Timber::e)
        }
    }

    fun updateMissionFilter(menuText: String) {
        val selectedFilter = MissionFilter.findFilterByText(text = menuText)

        val isCompleted = selectedFilter.isCompleted

        _state.update {
            it.copy(
                currentMissionFilter = selectedFilter
            )
        }

        fetchAppjamMissions(isCompleted)
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

    fun onEditMessageButtonClick() {
        viewModelScope.launch {
            _sideEffect.emit(AppjamtampSideEffect.NavigateToEdit)
        }
    }

    fun onMissionItemClick(mission: AppjamtampMissionUiModel) {
        viewModelScope.launch {
            if (_state.value.teamName.isNotBlank()) {
                _sideEffect.emit(AppjamtampSideEffect.NavigateToMissionDetail(mission))
            }
        }
    }

    fun onFloatingButtonClick() {
        viewModelScope.launch {
            _sideEffect.emit(AppjamtampSideEffect.NavigateToRanking)
        }
    }
}
