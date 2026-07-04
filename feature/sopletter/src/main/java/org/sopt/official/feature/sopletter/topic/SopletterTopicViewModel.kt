package org.sopt.official.feature.sopletter.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.sopletter.repository.SopletterRepository
import javax.inject.Inject

@HiltViewModel
class SopletterTopicViewModel @Inject constructor(
    private val sopletterRepository: SopletterRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SopletterTopicState())
    val uiState: StateFlow<SopletterTopicState> = _uiState.asStateFlow()

    init {
        fetchTopics()
    }

    private fun fetchTopics() = viewModelScope.launch {
        sopletterRepository.getTopics()
            .onSuccess { topics ->
                _uiState.update { state ->
                    state.copy(
                        topicList = topics.toPersistentList(),
                        isShowErrorDialog = false,
                    )
                }
            }
            .onFailure {
                _uiState.update { state ->
                    state.copy(isShowErrorDialog = true)
                }
            }
    }

    fun retryFetchTopics() {
        _uiState.update { state ->
            state.copy(isShowErrorDialog = false)
        }
        fetchTopics()
    }
}
