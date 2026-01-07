package org.sopt.official.feature.appjamtamp.teammisisonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionListEntity
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionListUiModel
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel
import org.sopt.official.feature.appjamtamp.teammisisonlist.model.AppjamtampMissionListState
import timber.log.Timber

@HiltViewModel
internal class AppjamtampTeamMissionListViewModel @Inject constructor(
    private val appjamtampRepository: AppjamtampRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<AppjamtampMissionListState> = MutableStateFlow(AppjamtampMissionListState.Loading)
    val state: StateFlow<AppjamtampMissionListState> = _state.asStateFlow()

    private val _teamName: MutableStateFlow<String> = MutableStateFlow("")
    val teamName: StateFlow<String> = _teamName.asStateFlow()

    fun fetchAppjamMissions(
        teamNumber: String
    ) {
        viewModelScope.launch {
            _state.value = AppjamtampMissionListState.Loading

            appjamtampRepository.getAppjamtampMissions(
                teamNumber = teamNumber,
                isCompleted = true
            ).onSuccess { missions ->
                _state.value = AppjamtampMissionListState.Success(
                    teamName = missions.teamName,
                    teamMissionList = missions.toUiModel()
                )
            }.onFailure(Timber::e)
        }
    }

    private fun AppjamtampMissionListEntity.toUiModel() = AppjamtampMissionListUiModel(
        teamNumber = teamNumber,
        teamName = teamName,
        missionList = missions.map {
            AppjamtampMissionUiModel(
                id = it.id,
                title = "${it.title} (${it.ownerName}이 작성한 미션)",
                ownerName = it.ownerName,
                level = MissionLevel.of(it.level),
                profileImage = it.profileImage,
                isCompleted = it.isCompleted,
            )
        }.toImmutableList()
    )
}
