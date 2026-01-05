package org.sopt.official.feature.appjamtamp.teammisisonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.feature.appjamtamp.missionlist.model.toUiModel
import org.sopt.official.feature.appjamtamp.teammisisonlist.model.AppjamtampMissionListState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppjamtampTeamMissionListViewModel @Inject constructor(
    private val appjamtampRepository: AppjamtampRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<AppjamtampMissionListState> = MutableStateFlow(AppjamtampMissionListState.Loading)
    val state: StateFlow<AppjamtampMissionListState> = _state.asStateFlow()

    private val _teamName: MutableStateFlow<String> = MutableStateFlow("")
    val teamName: StateFlow<String> = _teamName.asStateFlow()

    fun fetchAppjamMissions(
        teamNumber: String,
        isCompleted: Boolean? = null
    ) {
        viewModelScope.launch {
            _state.value = AppjamtampMissionListState.Loading

            appjamtampRepository.getAppjamtampMissions(
                teamNumber = teamNumber,
                isCompleted = isCompleted
            ).onSuccess { missions ->
                _state.value = AppjamtampMissionListState.Success(
                    teamName = missions.teamName,
                    teamMissionList = missions.toUiModel()
                )
            }.onFailure(Timber::e)
        }
    }
}
