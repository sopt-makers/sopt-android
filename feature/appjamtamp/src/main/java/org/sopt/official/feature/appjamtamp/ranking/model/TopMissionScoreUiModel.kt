package org.sopt.official.feature.appjamtamp.ranking.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionScore

data class TopMissionScoreListUiModel(
    val top10MissionScoreList: ImmutableList<TopMissionScoreUiModel>
)

internal fun List<AppjamtampMissionScore>.toUiModel(): TopMissionScoreListUiModel =
    TopMissionScoreListUiModel(
        top10MissionScoreList = this.map { it.toUiModel() }.toImmutableList()
    )

data class TopMissionScoreUiModel(
    val rank: Int,
    val teamName: String,
    val teamNumber: String,
    val todayPoints: Int,
    val totalPoints: Int
)

internal fun AppjamtampMissionScore.toUiModel(): TopMissionScoreUiModel =
    TopMissionScoreUiModel(
        rank = this.rank,
        teamName = this.teamName,
        teamNumber = this.teamNumber,
        todayPoints = this.todayPoints,
        totalPoints = this.totalPoints
    )
