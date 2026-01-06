package org.sopt.official.feature.appjamtamp.missiondetail

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.appjamtamp.missiondetail.model.DetailViewType
import org.sopt.official.feature.appjamtamp.missiondetail.model.StampClapUserModel
import org.sopt.official.feature.appjamtamp.model.ImageModel
import org.sopt.official.feature.appjamtamp.model.Mission
import org.sopt.official.feature.appjamtamp.model.User

internal data class MissionDetailState(
    val isLoading: Boolean = true,
    val isFailed: Boolean = false,

    val viewType: DetailViewType = DetailViewType.WRITE,
    val mission: Mission = Mission.DEFAULT,
    val imageModel: ImageModel = ImageModel.Empty,
    val date: String = "",
    val content: String = "",
    val header: String = "미션",

    val stampId: Int = -1,
    val writer: User = User(),
    val clapCount: Int = 0,
    val myClapCount: Int = 0,
    val unSyncedClapCount: Int = 0,
    val viewCount: Int = 0,

    val clappers: ImmutableList<StampClapUserModel> = persistentListOf()
)
