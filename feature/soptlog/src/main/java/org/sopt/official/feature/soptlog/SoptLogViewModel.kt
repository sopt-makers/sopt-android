package org.sopt.official.feature.soptlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.domain.soptlog.repository.SoptLogRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SoptLogViewModel @Inject constructor(
    private val soptLogRepository: SoptLogRepository,
) : ViewModel() {
    private val _soptLogInfo = MutableStateFlow(SoptLogState())
    val soptLogInfo: StateFlow<SoptLogState>
        get() = _soptLogInfo.asStateFlow()

    init {
        getSoptLogInfo()
    }

    private fun getSoptLogInfo() {
        viewModelScope.launch {
            _soptLogInfo.update {
                it.copy(
                    isLoading = true
                )
            }

            soptLogRepository.getSoptLogInfo()
                .onSuccess { info ->
                    _soptLogInfo.update {
                        it.copy(
                            soptLogInfo = info,
                            isError = false,
                        )
                    }
                }.onFailure { error ->
                    Timber.e(error)
                    _soptLogInfo.update {
                        it.copy(
                            isError = true,
                        )
                    }
                }

            _soptLogInfo.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }
}

data class SoptLogState(
    val soptLogInfo: SoptLogInfo = SoptLogInfo(
        profileImageUrl = "",
        userName = "",
        part = "",
        profileMessage = "",
        soptLevel = "",
        pokeCount = "",
        isActive = false,
        soptampRank = "",
        during = "",
        todayFortuneTitle = "",
    ),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
