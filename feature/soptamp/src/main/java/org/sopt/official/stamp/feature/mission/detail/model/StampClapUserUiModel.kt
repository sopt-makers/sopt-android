package org.sopt.official.stamp.feature.mission.detail.model

import org.sopt.official.domain.soptamp.model.StampClappers

data class StampClapUserUiModel(
    val nickname: String,
    val profileImage: String?,
    val profileMessage: String?,
    val clapCount: Int
)

fun StampClappers.StampClapUser.toUiModel() = StampClapUserUiModel(
    nickname = nickname,
    profileImage = profileImage,
    profileMessage = profileMessage,
    clapCount = clapCount
)