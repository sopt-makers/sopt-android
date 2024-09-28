package org.sopt.official.feature.fortune.feature.fortuneAmulet

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.fortune.usecase.GetTodayFortuneCardUseCase
import javax.inject.Inject

typealias GraphicColor = android.graphics.Color

@HiltViewModel
internal class FortuneAmuletViewModel @Inject constructor(
    getTodayFortuneCardUseCase: GetTodayFortuneCardUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<FortuneAmuletState> = MutableStateFlow(FortuneAmuletState())
    val state: StateFlow<FortuneAmuletState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                _state.value = _state.value.copy(isLoading = true)
                getTodayFortuneCardUseCase()
            }.onSuccess { todayFortuneCard ->
                _state.update {
                    it.copy(
                        description = todayFortuneCard.description,
                        imageColor = parseColor(todayFortuneCard.imageColorCode),
                        imageUrl = todayFortuneCard.imageUrl,
                        name = todayFortuneCard.name,
                        isLoading = false
                    )
                }
            }.onFailure {
                _state.value = _state.value.copy(isFailure = true)
            }
        }
    }

    private fun parseColor(colorCode: String): Color = try {
        Color(GraphicColor.parseColor(colorCode))
    } catch (e: IllegalArgumentException) {
        Color.White
    }
}
