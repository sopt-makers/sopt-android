package org.sopt.official.stamp.feature.ranking.part

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.soptamp.repository.RankingRepository
import org.sopt.official.stamp.feature.ranking.model.PartRankModel
import org.sopt.official.stamp.feature.ranking.model.toData
import javax.inject.Inject

@HiltViewModel
class PartRankingViewModel @Inject constructor(
    private val rankingRepository: RankingRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<PartRankingState> = MutableStateFlow(PartRankingState.Loading)
    val state: StateFlow<PartRankingState> = _state.asStateFlow()
    var isRefreshing by mutableStateOf(false)

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            fetchRanking()
        }
    }

    fun fetchRanking() = viewModelScope.launch {
        _state.value = PartRankingState.Loading
        rankingRepository.getPartRanking()
            .onSuccess { ranking ->
                if (isRefreshing) {
                    isRefreshing = false
                }
                onSuccessStateChange(ranking.toData().toImmutableList())
            }.onFailure {
                _state.value = PartRankingState.Failure
            }
    }

    private fun onSuccessStateChange(ranking: ImmutableList<PartRankModel>) {
        _state.value = PartRankingState.Success(ranking)
    }
}
