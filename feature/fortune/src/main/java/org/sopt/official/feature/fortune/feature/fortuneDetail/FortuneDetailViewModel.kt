package org.sopt.official.feature.fortune.feature.fortuneDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.fortune.usecase.GetTodayFortuneUseCase
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Error
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Loading
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.TodaySentence
import javax.inject.Inject

@HiltViewModel
internal class FortuneDetailViewModel @Inject constructor(
    getTodayFortuneUseCase: GetTodayFortuneUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<FortuneDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<FortuneDetailUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                getTodayFortuneUseCase()
            }.onSuccess { result ->
                _uiState.update {
                    TodaySentence(
                        userName = result.userName,
                        content = result.title,
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    Error(error)
                }
            }
        }
    }
}


