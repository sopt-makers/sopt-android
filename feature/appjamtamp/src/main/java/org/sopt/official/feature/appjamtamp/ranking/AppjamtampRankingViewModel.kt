package org.sopt.official.feature.appjamtamp.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.feature.appjamtamp.ranking.model.AppjamtampRankingState
import org.sopt.official.feature.appjamtamp.ranking.model.toUiModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class AppjamtampRankingViewModel @Inject constructor(
    private val appjamtampRepository: AppjamtampRepository
) : ViewModel() {

    private val _state: MutableStateFlow<AppjamtampRankingState> = MutableStateFlow(AppjamtampRankingState.Loading)
    val state: StateFlow<AppjamtampRankingState> = _state.asStateFlow()

    fun getRankingData() {
        viewModelScope.launch {
            _state.value = AppjamtampRankingState.Loading

            val top3Deferred = async { appjamtampRepository.getAppjamtampMissionTop3(size = 3) }
            val top10Deferred = async { appjamtampRepository.getAppjamtampMissionRanking(size = 12) } // size = 현재 기수 전체 앱잼 팀 수

            val top3Result = top3Deferred.await()
            val top10Result = top10Deferred.await()

            if (top3Result.isSuccess && top10Result.isSuccess) {
                _state.value = AppjamtampRankingState.Success(
                    top3RecentRankingListUiModel = top3Result.getOrThrow().toUiModel(),
                    top10MissionScoreListUiModel = top10Result.getOrThrow().toUiModel()
                )
            } else {
                val error = top3Result.exceptionOrNull() ?: top10Result.exceptionOrNull() ?: Exception("Unknown error")
                Timber.tag("AppjamtampRanking").e(error, "Failed to load ranking data")
                _state.value = AppjamtampRankingState.Failure(error)
            }
        }
    }
}
