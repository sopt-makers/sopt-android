/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.stamp.feature.ranking.part

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.soptamp.repository.RankingRepository
import org.sopt.official.stamp.feature.ranking.model.PartRankModel
import org.sopt.official.stamp.feature.ranking.model.toData

@ViewModelKey(PartRankingViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class PartRankingViewModel(
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

    fun fetchRanking() =
        viewModelScope.launch {
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