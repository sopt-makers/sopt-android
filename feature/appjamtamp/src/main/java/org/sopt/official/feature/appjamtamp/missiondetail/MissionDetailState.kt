package org.sopt.official.feature.appjamtamp.missiondetail

import org.sopt.official.feature.appjamtamp.missiondetail.model.DetailViewType
import org.sopt.official.feature.appjamtamp.model.ImageModel
import org.sopt.official.feature.appjamtamp.model.Mission
import org.sopt.official.feature.appjamtamp.model.User

internal data class MissionDetailState(
    val viewType: DetailViewType = DetailViewType.READ_ONLY,
    val mission: Mission = Mission.DEFAULT,
    val imageModel: ImageModel = ImageModel.Empty,
    val date: String = "",
    val content: String = "",

    val title: String = "",
    val writer: User = User(),
    val clapCount: Int = 0,
    val myClapCount: Int = 0,
    val unSyncedClapCount: Int = 0,
    val viewCount: Int = 0
)
