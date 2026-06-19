package org.sopt.official.feature.sopletter.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.feature.sopletter.write.model.SopletterWriteUiState
import javax.inject.Inject

@HiltViewModel
class SopletterWriteViewModel @Inject constructor(
    // TODO: 서버 연결 시 추가 예쩡
) : ViewModel() {

    private val _uiState = MutableStateFlow(SopletterWriteUiState())
    val uiState: StateFlow<SopletterWriteUiState>
        get() = _uiState.asStateFlow()

    fun setWriterName(name: String) {
        _uiState.update { it.copy(writerName = name) }
    }

    fun postSopletter() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

        }
    }
}