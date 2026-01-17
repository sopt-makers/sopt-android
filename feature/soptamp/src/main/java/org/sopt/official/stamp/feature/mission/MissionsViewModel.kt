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
package org.sopt.official.stamp.feature.mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.mypage.repository.UserRepository
import org.sopt.official.domain.soptamp.error.Error
import org.sopt.official.domain.soptamp.model.MissionsFilter
import org.sopt.official.domain.soptamp.repository.MissionsRepository
import org.sopt.official.domain.soptamp.repository.RankingRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.stamp.feature.mission.model.MissionListUiModel
import org.sopt.official.stamp.feature.mission.model.toUiModel
import timber.log.Timber

@ViewModelKey(MissionsViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class MissionsViewModel(
    private val missionsRepository: MissionsRepository,
    private val rankingRepository: RankingRepository,
    private val userRepository: UserRepository,
    private val stampRepository: StampRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<MissionsState> = MutableStateFlow(MissionsState.Loading)
    val state: StateFlow<MissionsState> = _state.asStateFlow()
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _generation = MutableStateFlow(-1)
    val generation = _generation.asStateFlow()
    val reportUrl = MutableStateFlow("")

    init {
        initUser()
        getReportUrl()
    }

    private fun initUser() {
        viewModelScope.launch {
            userRepository.getUserInfo()
                .onSuccess {
                    _nickname.value = it.nickname
                    _description.value = it.profileMessage
                }
                .onFailure(Timber::e)

            userRepository.getUserGeneration()
                .onSuccess { _generation.value = it }
                .onFailure(Timber::e)
        }
    }

    private fun getReportUrl() {
        viewModelScope.launch {
            stampRepository.getReportUrl()
                .onSuccess { reportUrl.value = it }
                .onFailure(Timber::e)
        }
    }

    fun fetchMissions(
        filter: String? = null,
        nickname: String = "",
    ) = viewModelScope.launch {
        _state.value = MissionsState.Loading
        fetchMissions(
            filter = filter?.let { MissionsFilter.findFilterOf(filter) } ?: MissionsFilter.ALL_MISSION,
            nickname = nickname,
        )
    }

    private suspend fun fetchMissions(
        filter: MissionsFilter,
        nickname: String,
    ) {
        if (nickname.isEmpty()) {
            val missions = when (filter) {
                MissionsFilter.ALL_MISSION -> missionsRepository.getAllMissions()
                MissionsFilter.COMPLETE_MISSION -> missionsRepository.getCompleteMissions()
                MissionsFilter.INCOMPLETE_MISSION -> missionsRepository.getInCompleteMissions()
            }
            missions.mapCatching { it.toUiModel(filter.title) }
                .onSuccess { result ->
                    _state.value = MissionsState.Success(result)
                }
                .onFailure { throwable ->
                    when (throwable) {
                        is Error.NetworkUnavailable -> {
                            _state.value = MissionsState.Failure(throwable)
                        }

                        else -> throw throwable
                    }
                }
            return
        }
        viewModelScope.launch {
            rankingRepository.getRankDetail(nickname)
                .onSuccess {
                    val missions =
                        it.userMissions
                            .map { mission -> mission.toUiModel() }
                    _state.value = MissionsState.Success(MissionListUiModel(filter.title, missions))
                }.onFailure { throwable ->
                    when (throwable) {
                        is Error.NetworkUnavailable -> {
                            _state.value = MissionsState.Failure(throwable)
                        }

                        else -> throw throwable
                    }
                }
        }
    }
}