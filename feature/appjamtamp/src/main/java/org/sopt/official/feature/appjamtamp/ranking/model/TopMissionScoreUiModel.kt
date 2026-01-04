package org.sopt.official.feature.appjamtamp.ranking.model

import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionScore

data class Top10MissionScoreListUiModel(
    val top10MissionScoreList: List<TopMissionScoreUiModel>
)

internal fun List<AppjamtampMissionScore>.toUiModel(): Top10MissionScoreListUiModel =
    Top10MissionScoreListUiModel(
        top10MissionScoreList = this.map { it.toUiModel() }
    )

data class TopMissionScoreUiModel(
    val rank: Int,
    val teamName: String,
    val todayPoints: Int,
    val totalPoints: Int
)

internal fun AppjamtampMissionScore.toUiModel(): TopMissionScoreUiModel =
    TopMissionScoreUiModel(
        rank = this.rank,
        teamName = this.teamName,
        todayPoints = this.todayPoints,
        totalPoints = this.totalPoints
    )
