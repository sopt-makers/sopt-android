/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.fortune.feature.fortuneDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.fortune.usecase.GetTodayFortuneUseCase
import org.sopt.official.domain.poke.repository.PokeRepository
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Error
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Loading
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success.TodaySentence
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success.UserInfo

@Inject
@ViewModelKey(FortuneDetailViewModel::class)
@ContributesIntoMap(AppScope::class)
internal class FortuneDetailViewModel @Inject constructor(
    private val getTodayFortuneUseCase: GetTodayFortuneUseCase,
    private val pokeRepository: PokeRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<FortuneDetailUiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<FortuneDetailUiState> get() = _uiState.asStateFlow()
    private var isAnonymous: Boolean = false
    // private var userId = DEFAULT_ID

    private var dummyUser = UserInfo(
        userId = 0L,
        profile = "",
        userName = "동민",
        generation = 111,
        part = "기획 파트"
    )

    init {
        updateUi()
    }

    fun updateUi() {
        viewModelScope.launch {
            runCatching {
                coroutineScope {
                    awaitAll(async { getTodayFortuneUseCase() } /*async { pokeRepository.getOnboardingPokeUserList(size = 1) }*/)
                }
            }.onSuccess { result ->
                val todayFortune = result[0]
                /*val pokeUser = result[1] as GetOnboardingPokeUserListResponse
                val user = pokeUser.data?.randomInfoList?.get(0)?.userInfoList?.get(0) ?: throw IllegalArgumentException()*/

                _uiState.update {
                    Success(
                        todaySentence = TodaySentence(
                            userName = todayFortune.userName,
                            content = todayFortune.title,
                        ), userInfo = UserInfo(
                            /*userId = user.userId.toLong(),
                            profile = user.profileImage,
                            userName = user.name,
                            generation = user.generation,
                            part = user.part,*/

                            userId = dummyUser.userId,
                            profile = dummyUser.profile,
                            userName = dummyUser.userName,
                            generation = dummyUser.generation,
                            part = dummyUser.part
                        )
                    )
                }/*.also {
                    userId = user.userId
                }*/
            }.onFailure { error ->
                _uiState.update { Error(error) }
            }
        }
    }

    fun poke(message: String) {
        viewModelScope.launch {
            runCatching {
                pokeRepository.pokeUser(
                    userId = dummyUser.userId.toInt(), /*userId = userId,*/ isAnonymous = isAnonymous, message = message
                )
            }.onSuccess {
                _uiState.update { uiState.value as Success }
            }.onFailure { error ->
                _uiState.update { Error(error) }
            }
        }
    }
}