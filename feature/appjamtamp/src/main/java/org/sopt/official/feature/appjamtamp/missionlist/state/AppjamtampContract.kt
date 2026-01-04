package org.sopt.official.feature.appjamtamp.missionlist.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel
import org.sopt.official.feature.appjamtamp.model.MissionFilter

data class AppjamtampMissionState(
    val reportUrl: String = "",
    val missionList: ImmutableList<AppjamtampMissionUiModel> = persistentListOf(),
    val currentMissionFilter: MissionFilter = MissionFilter.ALL
)

sealed interface AppjamtampSideEffect {
    data object NavigateToWebView : AppjamtampSideEffect
    data object NavigateToEdit : AppjamtampSideEffect
}