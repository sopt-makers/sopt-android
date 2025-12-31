package org.sopt.official.feature.appjamtamp.missionlist.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel

data class AppjamtampMissionState(
    val reportUrl : String = "",
    val missionList : ImmutableList<AppjamtampMissionUiModel> = persistentListOf()
)

sealed interface AppjamtampSideEffect {
    object NavigateToWebView : AppjamtampSideEffect
}