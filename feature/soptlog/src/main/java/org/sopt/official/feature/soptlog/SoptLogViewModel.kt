/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.soptlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.CheckNewInPoke
import org.sopt.official.domain.poke.usecase.CheckNewInPokeUseCase
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.domain.soptlog.repository.SoptLogRepository
import timber.log.Timber

@HiltViewModel
class SoptLogViewModel @Inject constructor(
    private val soptLogRepository: SoptLogRepository,
    private val checkNewInPokeUseCase: CheckNewInPokeUseCase,
) : ViewModel() {
    private val _soptLogInfo = MutableStateFlow(SoptLogState())
    val soptLogInfo: StateFlow<SoptLogState>
        get() = _soptLogInfo.asStateFlow()

    fun getSoptLogInfo() {
        viewModelScope.launch {
            _soptLogInfo.update {
                it.copy(
                    isLoading = true
                )
            }

            soptLogRepository.getSoptLogInfo()
                .onSuccess { info ->
                    _soptLogInfo.update {
                        it.copy(
                            soptLogInfo = info,
                            isError = false,
                        )
                    }
                }.onFailure { error ->
                    Timber.e(error)
                    _soptLogInfo.update {
                        it.copy(
                            isError = true,
                        )
                    }
                }

            _soptLogInfo.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    // 신규 유저인지 판단하는 함수 (콕 찌르기 온보딩)
    suspend fun fetchIsNewPoke(): Result<Boolean> {
        _soptLogInfo.update { it.copy(isLoading = true) }

        return when (val apiResult: ApiResult<*> = checkNewInPokeUseCase()) {
            is ApiResult.Success -> {
                _soptLogInfo.update { it.copy(isLoading = false) }
                Result.success((apiResult as ApiResult.Success<CheckNewInPoke>).data.isNew)
            }

            is ApiResult.ApiError -> {
                _soptLogInfo.update { it.copy(isLoading = false) }
                Result.failure(Exception("API Error: ${apiResult.statusCode} - ${apiResult.responseMessage}"))
            }

            is ApiResult.Failure -> {
                _soptLogInfo.update { it.copy(isLoading = false) }
                Result.failure(apiResult.throwable)
            }
        }
    }
}

data class SoptLogState(
    val soptLogInfo: SoptLogInfo = SoptLogInfo(
        isActive = false,
        soptampCount = 0,
        viewCount = 0,
        myClapCount = 0,
        clapCount = 0,
        pokeCount = 0,
        newFriendsPokeCount = 0,
        bestFriendsPokeCount = 0,
        soulmatesPokeCount = 0
    ),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
