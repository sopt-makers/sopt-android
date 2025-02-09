package org.sopt.official.feature.soptlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.domain.soptlog.repository.SoptLogRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SoptLogViewModel @Inject constructor(
    private val soptLogRepository: SoptLogRepository,
) : ViewModel() {
    private val _soptLogInfo = MutableStateFlow<Result<SoptLogInfo>>(Result.failure(Exception()))
    val soptLogInfo: StateFlow<Result<SoptLogInfo>>
        get() = _soptLogInfo.asStateFlow()

    init {
        getSoptLogInfo()
    }

    private fun getSoptLogInfo() {
        viewModelScope.launch {
            _soptLogInfo.value = soptLogRepository.getSoptLogInfo()

            with(soptLogInfo.value.exceptionOrNull()) {
                if (this != null){
                    Timber.e(this)
                }
            }
        }
    }
}
