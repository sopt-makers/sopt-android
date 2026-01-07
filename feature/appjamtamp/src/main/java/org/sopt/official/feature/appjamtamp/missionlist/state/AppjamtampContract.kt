package org.sopt.official.feature.appjamtamp.missionlist.state

import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionListUiModel
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel
import org.sopt.official.feature.appjamtamp.model.MissionFilter

data class AppjamtampMissionState(
    val reportUrl: String = "",
    val teamName: String = "",
    val missionList: AppjamtampMissionListUiModel = AppjamtampMissionListUiModel(),
    val currentMissionFilter: MissionFilter = MissionFilter.APPJAM
)

sealed interface AppjamtampSideEffect {
    data object NavigateToWebView : AppjamtampSideEffect
    data object NavigateToEdit : AppjamtampSideEffect
    data class NavigateToMissionDetail(val mission: AppjamtampMissionUiModel) : AppjamtampSideEffect
    data object NavigateToRanking : AppjamtampSideEffect
}
