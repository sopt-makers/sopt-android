package org.sopt.official.feature.fortune.feature.fortuneDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.fortune.model.TodayFortuneWord
import org.sopt.official.domain.fortune.usecase.GetTodayFortuneUseCase
import org.sopt.official.domain.poke.entity.GetOnboardingPokeUserListResponse
import org.sopt.official.domain.poke.repository.PokeRepository
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Error
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Loading
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success.TodaySentence
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success.UserInfo
import javax.inject.Inject

@HiltViewModel
internal class FortuneDetailViewModel @Inject constructor(
    getTodayFortuneUseCase: GetTodayFortuneUseCase,
    private val pokeRepository: PokeRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<FortuneDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<FortuneDetailUiState> get() = _uiState.asStateFlow()

    private var isAnonymous: Boolean = false
    private var userId = -1

    init {
        viewModelScope.launch {
            runCatching {
                coroutineScope {
                    awaitAll(
                        async { getTodayFortuneUseCase() },
                        async { pokeRepository.getOnboardingPokeUserList(size = 1) }
                    )
                }
            }.onSuccess { result ->
                val todayFortune = result[0] as TodayFortuneWord
                val pokeUser = result[1] as GetOnboardingPokeUserListResponse
                val user = pokeUser.data?.randomInfoList?.get(0)?.userInfoList?.get(0) ?: throw IllegalArgumentException()
                userId = user.userId
                _uiState.update {
                    Success(
                        todaySentence = TodaySentence(
                            userName = todayFortune.userName,
                            content = todayFortune.title,
                        ),
                        userInfo = UserInfo(
                            userId = user.userId.toLong(),
                            profile = user.profileImage,
                            userName = user.name,
                            generation = user.generation,
                            part = user.part,
                        )
                    )
                }
            }.onFailure { error ->
                _uiState.update { Error(error) }
            }
        }
    }

    fun poke(message: String) {
        viewModelScope.launch {
            runCatching {
                pokeRepository.pokeUser(
                    userId = userId,
                    isAnonymous = isAnonymous,
                    message = message
                )
            }.onSuccess {
                _uiState.update { uiState.value as Success }
            }.onFailure { error ->
                _uiState.update { Error(error) }
            }
        }
    }
}
