package org.sopt.official.feature.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.schedule.model.Schedule
import org.sopt.official.domain.schedule.repository.ScheduleRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
) : ViewModel() {
    private val _schedule = MutableStateFlow(ScheduleState())
    val schedule: StateFlow<ScheduleState>
        get() = _schedule.asStateFlow()

    init {
        getScheduleList()
    }

    private fun getScheduleList() {
        viewModelScope.launch {
            _schedule.update {
                it.copy(isLoading = true)
            }
            scheduleRepository.getScheduleList().onSuccess { scheduleList ->
                _schedule.update {
                    it.copy(
                        scheduleList = scheduleList.toPersistentList(),
                        isLoading = false,
                    )
                }
            }.onFailure { error ->
                Timber.e(error)
                _schedule.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                    )
                }
            }
        }
    }
}

data class ScheduleState(
    val scheduleList: ImmutableList<Schedule> = persistentListOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
