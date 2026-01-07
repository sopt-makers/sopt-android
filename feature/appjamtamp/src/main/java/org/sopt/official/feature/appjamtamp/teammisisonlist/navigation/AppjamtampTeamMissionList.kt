package org.sopt.official.feature.appjamtamp.teammisisonlist.navigation

import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

@Serializable
internal data class AppjamtampTeamMissionList(
    val teamNumber: String
) : Route
