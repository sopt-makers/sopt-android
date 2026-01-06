package org.sopt.official.feature.appjamtamp.missiondetail.model

import org.sopt.official.domain.soptamp.model.StampClappers

data class StampClapUserModel(
    val nickname: String,
    val profileImage: String?,
    val profileMessage: String?,
    val clapCount: Int
)

fun StampClappers.StampClapUser.toUiModel() = StampClapUserModel(
    nickname = nickname,
    profileImage = profileImage,
    profileMessage = profileMessage,
    clapCount = clapCount
)
