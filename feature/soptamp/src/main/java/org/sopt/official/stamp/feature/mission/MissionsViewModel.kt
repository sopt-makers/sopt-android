/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.stamp.data.local.SoptampDataStore
import org.sopt.official.stamp.domain.error.Error
import org.sopt.official.stamp.domain.model.MissionsFilter
import org.sopt.official.stamp.domain.repository.MissionsRepository
import org.sopt.stamp.feature.mission.model.toUiModel
import javax.inject.Inject

@HiltViewModel
class MissionsViewModel @Inject constructor(
    private val missionsRepository: MissionsRepository,
    private val dataStore: SoptampDataStore,
) : ViewModel() {

    private val _state: MutableStateFlow<MissionsState> = MutableStateFlow(MissionsState.Loading)
    val state: StateFlow<MissionsState> = _state.asStateFlow()
    val nickname = dataStore.nickname

    fun fetchMissions(filter: String? = null) = viewModelScope.launch {
        _state.value = MissionsState.Loading
        fetchMissions(
            filter = filter?.let { MissionsFilter.findFilterOf(filter) } ?: MissionsFilter.ALL_MISSION
        )
    }

    private suspend fun fetchMissions(filter: MissionsFilter) {
        val missions = when (filter) {
            MissionsFilter.ALL_MISSION -> missionsRepository.getAllMissions()
            MissionsFilter.COMPLETE_MISSION -> missionsRepository.getCompleteMissions()
            MissionsFilter.INCOMPLETE_MISSION -> missionsRepository.getInCompleteMissions()
        }
        missions.mapCatching { it.toUiModel(filter.title) }
            .onSuccess { missions ->
                _state.value = MissionsState.Success(missions)
            }
            .onFailure { throwable ->
                when (throwable) {
                    is Error.NetworkUnavailable -> {
                        _state.value = MissionsState.Failure(throwable)
                    }

                    else -> throw throwable
                }
            }
    }
}
