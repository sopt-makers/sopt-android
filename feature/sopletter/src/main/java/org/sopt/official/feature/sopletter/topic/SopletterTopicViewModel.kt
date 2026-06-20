package org.sopt.official.feature.sopletter.topic

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SopletterTopicViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(SopletterTopicState())
    val uiState: StateFlow<SopletterTopicState> = _uiState.asStateFlow()

}