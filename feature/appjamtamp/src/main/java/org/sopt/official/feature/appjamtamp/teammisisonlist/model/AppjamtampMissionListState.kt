package org.sopt.official.feature.appjamtamp.teammisisonlist.model

import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionListUiModel

sealed class AppjamtampMissionListState {
    data object Loading : AppjamtampMissionListState()

    data class Success(
        val teamName: String,
        val teamMissionList: AppjamtampMissionListUiModel,
    ) : AppjamtampMissionListState()

    data class Failure(val error: Throwable) : AppjamtampMissionListState()
}
