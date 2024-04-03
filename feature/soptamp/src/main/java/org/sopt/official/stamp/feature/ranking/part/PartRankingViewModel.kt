package org.sopt.official.stamp.feature.ranking.part

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.stamp.feature.ranking.model.PartRankModel

@HiltViewModel
class PartRankingViewModel @Inject constructor() : ViewModel() {
    private val _state: MutableStateFlow<PartRankingState> = MutableStateFlow(PartRankingState.Loading)
    val state: StateFlow<PartRankingState> = _state.asStateFlow()
    var isRefreshing by mutableStateOf(false)

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            // TODO: Fetch Data
        }
    }

    private fun onSuccessStateChange(ranking: ImmutableList<PartRankModel>) {
        _state.value = PartRankingState.Success(ranking)
    }
}
