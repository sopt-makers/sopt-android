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
package org.sopt.official.feature.appjamtamp.missionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel
import org.sopt.official.feature.appjamtamp.missionlist.model.toUiModel
import org.sopt.official.feature.appjamtamp.missionlist.state.AppjamtampMissionState
import org.sopt.official.feature.appjamtamp.missionlist.state.AppjamtampSideEffect
import org.sopt.official.feature.appjamtamp.model.MissionFilter
import timber.log.Timber

@ViewModelKey(AppjamtampMissionViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class AppjamtampMissionViewModel(
    private val appjamtampRepository: AppjamtampRepository,
    private val stampRepository: StampRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AppjamtampMissionState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AppjamtampSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        getReportUrl()
    }

    fun fetchAppjamMissions(isCompleted: Boolean? = null) {
        viewModelScope.launch {
            appjamtampRepository.getAppjamtampMissions(
                isCompleted = isCompleted
            ).onSuccess { missions ->
                _state.update { currentState ->
                    currentState.copy(
                        teamName = missions.teamName,
                        missionList = missions.toUiModel()
                    )
                }
            }.onFailure(Timber::e)
        }
    }

    fun updateMissionFilter(menuText: String) {
        val selectedFilter = MissionFilter.findFilterByText(text = menuText)

        val isCompleted = selectedFilter.isCompleted

        _state.update {
            it.copy(
                currentMissionFilter = selectedFilter
            )
        }

        fetchAppjamMissions(isCompleted)
    }

    private fun getReportUrl() {
        viewModelScope.launch {
            stampRepository.getReportUrl()
                .onSuccess {
                    _state.update { currentState ->
                        currentState.copy(
                            reportUrl = it
                        )
                    }
                }
                .onFailure(Timber::e)
        }
    }

    fun reportButtonClick() {
        viewModelScope.launch {
            _sideEffect.emit(AppjamtampSideEffect.NavigateToWebView)
        }
    }

    fun onEditMessageButtonClick() {
        viewModelScope.launch {
            _sideEffect.emit(AppjamtampSideEffect.NavigateToEdit)
        }
    }

    fun onMissionItemClick(mission: AppjamtampMissionUiModel) {
        viewModelScope.launch {
            if (_state.value.teamName.isNotBlank()) {
                _sideEffect.emit(AppjamtampSideEffect.NavigateToMissionDetail(mission))
            }
        }
    }

    fun onFloatingButtonClick() {
        viewModelScope.launch {
            _sideEffect.emit(AppjamtampSideEffect.NavigateToRanking)
        }
    }
}