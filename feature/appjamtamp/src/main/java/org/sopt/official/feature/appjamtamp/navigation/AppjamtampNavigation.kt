package org.sopt.official.feature.appjamtamp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import org.sopt.official.feature.appjamtamp.missiondetail.navigation.AppjamtampMissionDetail
import org.sopt.official.feature.appjamtamp.missionlist.navigation.AppjamtampMissionList
import org.sopt.official.feature.appjamtamp.ranking.navigation.AppjamtampRanking
import org.sopt.official.feature.appjamtamp.teammisisonlist.navigation.AppjamtampTeamMissionList

fun NavController.navigateToAppjamtamp(navOptions: NavOptions? = null) {
    navigate(AppjamtampNavGraph, navOptions)
}

internal fun NavController.navigateToMissionList(navOptions: NavOptions? = null) {
    navigate(AppjamtampMissionList, navOptions)
}

internal fun NavController.navigateToMissionDetail(navOptions: NavOptions? = null) {
    navigate(AppjamtampMissionDetail, navOptions)
}

internal fun NavController.navigateToRanking(navOptions: NavOptions? = null) {
    navigate(AppjamtampRanking, navOptions)
}

internal fun NavController.navigateToTeamMissionList(
    teamNumber: String,
    navOptions: NavOptions? = null
) {
    navigate(AppjamtampTeamMissionList(teamNumber), navOptions)
}
