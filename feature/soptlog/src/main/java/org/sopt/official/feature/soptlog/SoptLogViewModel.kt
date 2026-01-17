/*
 * MIT License
 * Copyright 2024-2026 SOPT - Shout Our Passion Together
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

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.CheckNewInPoke
import org.sopt.official.domain.poke.usecase.CheckNewInPokeUseCase
import org.sopt.official.domain.soptlog.repository.SoptLogRepository
import org.sopt.official.feature.soptlog.navigation.SoptLogUrl
import org.sopt.official.feature.soptlog.state.SoptLogNavigationEvent
import org.sopt.official.feature.soptlog.state.SoptLogState
import timber.log.Timber

@ViewModelKey(SoptLogViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class SoptLogViewModel(
    private val soptLogRepository: SoptLogRepository,
    private val appjamtampRepository: AppjamtampRepository,
    private val checkNewInPokeUseCase: CheckNewInPokeUseCase,
) : ViewModel() {
    private val _soptLogInfo = MutableStateFlow(SoptLogState())
    val soptLogInfo: StateFlow<SoptLogState>
        get() = _soptLogInfo.asStateFlow()

    val todayFortuneText = _soptLogInfo.map { it.soptLogInfo.todayFortuneText }

    private val _isAppjamJoined = MutableStateFlow(false)
    val isAppjamJoined: StateFlow<Boolean>
        get() = _isAppjamJoined.asStateFlow()

    private val _navigationEvent = Channel<SoptLogNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onNavigationClick(url: String) {
        when (SoptLogUrl.from(url)) {
            SoptLogUrl.POKE, SoptLogUrl.POKE_FRIEND_SUMMARY -> {
                handlePokeNavigation(url)
            }
            SoptLogUrl.SOPTAMP -> {
                viewModelScope.launch {
                    _navigationEvent.send(
                        SoptLogNavigationEvent.NavigateToDeepLink(url)
                    )
                }
            }
            else -> {}
        }
    }

    private fun handlePokeNavigation(url: String) {
        viewModelScope.launch {
            _soptLogInfo.update { it.copy(isLoading = true) }

            fetchIsNewPoke()
                .onSuccess { isNewPoke ->
                    val uri = url.toUri()
                    val type = uri.getQueryParameter("type")

                    _navigationEvent.send(
                        SoptLogNavigationEvent.NavigateToPoke(
                            url = url,
                            isNewPoke = isNewPoke,
                            friendType = type
                        )
                    )
                }
                .onFailure(Timber::e)

            _soptLogInfo.update { it.copy(isLoading = false) }
        }
    }

    // 나의 앱잼 정보도 같이 가져옴 (앱잼탬프 기간만)
    fun getSoptLogInfoData() {
        viewModelScope.launch {
            _soptLogInfo.update { it.copy(isLoading = true) }

            val soptLogDeferred = async { soptLogRepository.getSoptLogInfo() }
            val appjamInfoDeferred = async { appjamtampRepository.getMyAppjamInfo() }

            val soptLogResult = soptLogDeferred.await()
            val appjamResult = appjamInfoDeferred.await()

            if (soptLogResult.isSuccess && appjamResult.isSuccess) {
                _soptLogInfo.update {
                    it.copy(
                        soptLogInfo = soptLogResult.getOrThrow(),
                        isLoading = false,
                        isError = false
                    )
                }
                _isAppjamJoined.value = appjamResult.getOrThrow().isAppjamJoined
            } else {
                val error = soptLogResult.exceptionOrNull() ?: appjamResult.exceptionOrNull() ?: Exception("Unknown error")
                Timber.e(error)
                _soptLogInfo.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

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
        val apiResult: ApiResult<*> = checkNewInPokeUseCase()

        return when (apiResult) {
            is ApiResult.Success -> {
                Result.success((apiResult as ApiResult.Success<CheckNewInPoke>).data.isNew)
            }

            is ApiResult.ApiError -> {
                Result.failure(Exception("API Error: ${apiResult.statusCode} - ${apiResult.responseMessage}"))
            }

            is ApiResult.Failure -> {
                Result.failure(apiResult.throwable)
            }
        }
    }
}