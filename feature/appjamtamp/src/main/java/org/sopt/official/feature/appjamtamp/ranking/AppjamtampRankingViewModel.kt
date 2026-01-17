/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.feature.appjamtamp.ranking.model.AppjamtampRankingState
import org.sopt.official.feature.appjamtamp.ranking.model.toUiModel
import timber.log.Timber

@Inject
@ViewModelKey(AppjamtampRankingViewModel::class)
@ContributesIntoMap(AppScope::class)
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
                    topMissionScoreListUiModel = top10Result.getOrThrow().toUiModel()
                )
            } else {
                val error = top3Result.exceptionOrNull() ?: top10Result.exceptionOrNull() ?: Exception("Unknown error")
                Timber.tag("AppjamtampRanking").e(error, "Failed to load ranking data")
                _state.value = AppjamtampRankingState.Failure(error)
            }
        }
    }
}