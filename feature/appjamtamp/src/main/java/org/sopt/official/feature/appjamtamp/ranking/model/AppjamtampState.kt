package org.sopt.official.feature.appjamtamp.ranking.model

sealed class AppjamtampRankingState {
    data object Loading : AppjamtampRankingState()

    data class Success(
        val top3RecentRankingListUiModel: Top3RecentRankingListUiModel,
        val topMissionScoreListUiModel: TopMissionScoreListUiModel
    ) : AppjamtampRankingState()

    data class Failure(val error: Throwable) : AppjamtampRankingState()
}
