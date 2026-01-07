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
package org.sopt.official.feature.appjamtamp.teammisisonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionListEntity
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionListUiModel
import org.sopt.official.feature.appjamtamp.missionlist.model.AppjamtampMissionUiModel
import org.sopt.official.feature.appjamtamp.teammisisonlist.model.AppjamtampMissionListState
import timber.log.Timber

@HiltViewModel
internal class AppjamtampTeamMissionListViewModel @Inject constructor(
    private val appjamtampRepository: AppjamtampRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<AppjamtampMissionListState> = MutableStateFlow(AppjamtampMissionListState.Loading)
    val state: StateFlow<AppjamtampMissionListState> = _state.asStateFlow()

    private val _teamName: MutableStateFlow<String> = MutableStateFlow("")
    val teamName: StateFlow<String> = _teamName.asStateFlow()

    fun fetchAppjamMissions(
        teamNumber: String
    ) {
        viewModelScope.launch {
            _state.value = AppjamtampMissionListState.Loading

            appjamtampRepository.getAppjamtampMissions(
                teamNumber = teamNumber,
                isCompleted = true
            ).onSuccess { missions ->
                _state.value = AppjamtampMissionListState.Success(
                    teamName = missions.teamName,
                    teamMissionList = missions.toUiModel()
                )
            }.onFailure(Timber::e)
        }
    }

    private fun AppjamtampMissionListEntity.toUiModel() = AppjamtampMissionListUiModel(
        teamNumber = teamNumber,
        teamName = teamName,
        missionList = missions.map {
            AppjamtampMissionUiModel(
                id = it.id,
                title = "${it.title} (${it.ownerName}이 작성한 미션)",
                ownerName = it.ownerName,
                level = MissionLevel.of(it.level),
                profileImage = it.profileImage,
                isCompleted = it.isCompleted,
            )
        }.toImmutableList()
    )
}
