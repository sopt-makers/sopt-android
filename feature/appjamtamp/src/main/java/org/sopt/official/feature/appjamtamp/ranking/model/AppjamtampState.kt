package org.sopt.official.feature.appjamtamp.ranking.model

sealed class AppjamtampRankingState {
    data object Loading : AppjamtampRankingState()

    data class Success(
        val top3RecentRankingListUiModel: Top3RecentRankingListUiModel,
        val top10MissionScoreListUiModel: Top10MissionScoreListUiModel
    ) : AppjamtampRankingState()

    data class Failure(val error: Throwable) : AppjamtampRankingState()
}
