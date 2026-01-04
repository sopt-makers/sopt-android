package org.sopt.official.feature.appjamtamp.ranking.model

import org.sopt.official.domain.appjamtamp.entity.AppjamtampRecentMission

data class Top3RecentRankingListUiModel(
    val top3RecentRankingList: List<Top3RecentRankingUiModel>
)

internal fun List<AppjamtampRecentMission>.toUiModel(): Top3RecentRankingListUiModel =
    Top3RecentRankingListUiModel(
        top3RecentRankingList = this.map { it.toUiModel() }
    )

data class Top3RecentRankingUiModel(
    val stampId: Long,
    val missionId: Long,
    val userId: Long,
    val imageUrl: String,
    val createdAt: String?,
    val userName: String,
    val userProfileImage: String?,
    val teamName: String,
    val teamNumber: String
)

internal fun AppjamtampRecentMission.toUiModel(): Top3RecentRankingUiModel =
    Top3RecentRankingUiModel(
        stampId = this.stampId,
        missionId = this.missionId,
        userId = this.userId,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt,
        userName = this.userName,
        userProfileImage = this.userProfileImage,
        teamName = this.teamName,
        teamNumber = this.teamNumber
    )
