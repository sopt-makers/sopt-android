package org.sopt.official.feature.sopletter.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import javax.inject.Inject

@HiltViewModel
class SopletterMainViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(SopletterMainUiState())
    val uiState: StateFlow<SopletterMainUiState> = _uiState.asStateFlow()
}
