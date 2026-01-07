package org.sopt.official.feature.appjamtamp.missiondetail

internal sealed interface MissionDetailSideEffect {
    data object NavigateUp : MissionDetailSideEffect
}
