/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.ranking.rank

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.mypage.user.GetNicknameUseCase
import org.sopt.official.domain.soptamp.model.RankFetchType
import org.sopt.official.domain.soptamp.repository.RankingRepository
import org.sopt.official.stamp.feature.ranking.model.RankingListUiModel
import org.sopt.official.stamp.feature.ranking.model.toUiModel

@Inject
@ViewModelKey(RankingViewModel::class)
@ContributesIntoMap(AppScope::class)
class RankingViewModel
@Inject
constructor(
    private val rankingRepository: RankingRepository,
    private val getNicknameUseCase: GetNicknameUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<RankingState> = MutableStateFlow(RankingState.Loading)
    val state: StateFlow<RankingState> = _state.asStateFlow()
    var isRefreshing by mutableStateOf(false)
    val nickname = getNicknameUseCase()

    fun onRefresh(
        isCurrent: Boolean,
        part: String,
    ) {
        viewModelScope.launch {
            isRefreshing = true
            fetchRanking(isCurrent, part)
        }
    }

    fun fetchRanking(
        isCurrent: Boolean,
        part: String,
    ) = viewModelScope.launch {
        _state.value = RankingState.Loading

        if (isCurrent) {
            fetchCurrentRanking()
        } else {
            runCatching {
                Part.getPartName(part)
            }.onSuccess { partName ->
                fetchPartRanking(partName)
            }.onFailure {
                _state.value = RankingState.Failure
            }
        }
    }

    private suspend fun fetchCurrentRanking() {
        rankingRepository.getRanking(
            RankFetchType.Term(),
        ).mapCatching { it.toUiModel() }
            .onSuccess { ranking ->
                if (isRefreshing) {
                    isRefreshing = false
                }
                onSuccessStateChange(ranking)
            }.onFailure {
                _state.value = RankingState.Failure
            }
    }

    private suspend fun fetchPartRanking(part: String) {
        rankingRepository.getCurrentPartRanking(
            part,
        ).mapCatching { it.toUiModel() }.onSuccess { ranking ->
            if (isRefreshing) {
                isRefreshing = false
            }
            onSuccessStateChange(ranking)
        }.onFailure {
            _state.value = RankingState.Failure
        }
    }

    enum class Part(val part: String) {
        PLAN("기획"),
        DESIGN("디자인"),
        WEB("웹"),
        IOS("아요"),
        ANDROID("안드"),
        SERVER("서버"),
        ;

        companion object {
            fun getPartName(part: String): String {
                return entries.find { it.part == part }?.name ?: throw IllegalArgumentException("Wrong Part Name : $part")
            }
        }
    }

    private fun onSuccessStateChange(ranking: RankingListUiModel) {
        _state.value = RankingState.Success(ranking, getNicknameUseCase())
    }
}