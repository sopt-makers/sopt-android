package org.sopt.official.feature.soptlog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.domain.soptlog.repository.SoptLogRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SoptLogViewModel @Inject constructor(
    private val soptLogRepository: SoptLogRepository,
) : ViewModel() {
    private val _soptLogInfo = mutableStateOf<SoptLogInfo?>(null)
    val soptLogInfo
        get() = _soptLogInfo.value

    init {
        getSoptLogInfo()
    }

    private fun getSoptLogInfo() {
        viewModelScope.launch {
            soptLogRepository.getSoptLogInfo().onSuccess {
                _soptLogInfo.value = it
            }.onFailure(Timber::e)
        }
    }
}
