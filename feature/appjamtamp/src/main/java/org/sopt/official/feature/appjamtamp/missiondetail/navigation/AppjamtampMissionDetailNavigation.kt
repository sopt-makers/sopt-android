package org.sopt.official.feature.appjamtamp.missiondetail.navigation

import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

@Serializable
internal data class AppjamtampMissionDetail(
    val missionId: Int = -1,
    val missionLevel: Int = 1,
    val title: String = "",
    val ownerName: String? = null
) : Route
