/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Instant
import org.sopt.official.R
import org.sopt.official.auth.model.UserActiveState
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.util.calculateDurationOfGeneration
import org.sopt.official.common.util.calculateGenerationStartDate
import org.sopt.official.common.util.systemNow
import org.sopt.official.common.util.toDefaultLocalDate
import org.sopt.official.domain.entity.home.HomeSection
import org.sopt.official.domain.entity.home.SoptActiveGeneration
import org.sopt.official.domain.entity.home.SoptActiveRecord
import org.sopt.official.domain.entity.home.SoptUser
import org.sopt.official.domain.entity.home.User
import org.sopt.official.domain.poke.entity.CheckNewInPoke
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.usecase.CheckNewInPokeUseCase
import org.sopt.official.domain.repository.home.HomeRepository
import org.sopt.official.domain.usecase.notification.RegisterPushTokenUseCase
import org.sopt.official.feature.home.model.AppServiceEnum
import org.sopt.official.feature.home.model.AppServiceState
import org.sopt.official.feature.home.model.HomeCTAType
import org.sopt.official.feature.home.model.HomeMenuType
import org.sopt.official.feature.home.model.SoptMainContentUrl
import org.sopt.official.feature.home.model.UserActiveGeneration
import org.sopt.official.feature.home.model.UserUiState
import org.sopt.official.feature.poke.UiState
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val registerPushTokenUseCase: RegisterPushTokenUseCase,
    private val checkNewInPokeUseCase: CheckNewInPokeUseCase,
) : ViewModel() {
    private val homeUiState = MutableStateFlow(SoptUser())
    private val user = homeUiState.map { it.user }

    private val _description = MutableStateFlow(HomeSection())
    val description = _description.asStateFlow()
    val userActiveState =
        homeUiState
            .map { it.user.activeState }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UserActiveState.UNAUTHENTICATED)
    val isAllNotificationsConfirm: Flow<Boolean> = homeUiState.map { it.isAllConfirm }
    val generationList: Flow<SoptActiveGeneration?> =
        homeUiState
            .filterNotNull()
            .map { it.user.generationList }
    val title =
        user
            .map {
                val state = it.activeState
                val userName = it.name
                val generation = it.generationList?.last?.toInt() ?: 0
                val startDate = calculateGenerationStartDate(generation)
                val currentDate = Instant.systemNow().toDefaultLocalDate()
                val period = calculateDurationOfGeneration(startDate, currentDate)
                when {
                    userName?.isNotEmpty() == true ->
                        UserUiState.User(
                            R.string.main_title_member,
                            userName,
                            period.toString(),
                        )

                    state == UserActiveState.INACTIVE ->
                        UserUiState.InactiveUser(
                            R.string.main_title_inactive_member,
                        )

                    else -> UserUiState.UnauthenticatedUser(R.string.main_title_non_member)
                }
            }
    val generatedTagText: Flow<UserActiveGeneration> =
        user
            .map {
                val state = it.activeState
                val generationList = it.generationList
                val isEmpty = generationList?.isEmpty
                val lastGeneration = if (isEmpty != true) generationList?.first.toString() else ""
                when {
                    state == UserActiveState.ACTIVE ->
                        UserActiveGeneration(
                            R.string.main_active_member,
                            lastGeneration,
                        )

                    isEmpty == true && state == UserActiveState.INACTIVE ->
                        UserActiveGeneration(
                            R.string.main_no_profile_member,
                            null,
                        )

                    isEmpty != true && state == UserActiveState.INACTIVE ->
                        UserActiveGeneration(
                            R.string.main_inactive_member,
                            lastGeneration,
                        )

                    else -> UserActiveGeneration(R.string.main_non_member, null)
                }
            }
    val mainMenuContents: Flow<SoptMainContentUrl> =
        homeUiState
            .map {
                when (it.user.activeState) {
                    UserActiveState.UNAUTHENTICATED ->
                        SoptMainContentUrl(
                            HomeCTAType.SOPT_OFFICIAL_PAGE_URL,
                            HomeMenuType.SOPT_REVIEW_AUTHENTICATED_URL,
                            HomeMenuType.SOPT_PROJECT_AUTHENTICATED_URL,
                        )

                    UserActiveState.ACTIVE ->
                        SoptMainContentUrl(
                            HomeCTAType.SOPT_ATTENDANCE,
                            HomeMenuType.SOPT_CREW_ACTIVE_URL,
                            HomeMenuType.SOPT_PLAYGROUND_ACTIVE_URL,
                        )

                    else ->
                        SoptMainContentUrl(
                            HomeCTAType.SOPT_PLAYGROUND_URL,
                            HomeMenuType.SOPT_CREW_INACTIVE_URL,
                            HomeMenuType.SOPT_MEMBER_INACTIVE_URL,
                        )
                }
            }
    val subMenuContents =
        homeUiState
            .map {
                when (it.user.activeState) {
                    UserActiveState.ACTIVE ->
                        listOf(
                            HomeMenuType.SOPT_MEMBER_ACTIVE_URL,
                            HomeMenuType.SOPT_PROJECT_ACTIVE_URL,
                            HomeMenuType.SOPT_OFFICIAL_PAGE_ACTIVE_URL,
                        )

                    UserActiveState.INACTIVE ->
                        listOf(
                            HomeMenuType.SOPT_PROJECT_INACTIVE_URL,
                            HomeMenuType.SOPT_OFFICIAL_PAGE_INACTIVE_URL,
                            HomeMenuType.SOPT_INSTAGRAM_INACTIVE_URL,
                            HomeMenuType.SOPT_YOUTUBE_INACTIVE_URL,
                        )

                    else ->
                        listOf(
                            HomeMenuType.SOPT_INSTAGRAM_AUTHENTICATED_URL,
                            HomeMenuType.SOPT_YOUTUBE_AUTHENTICATED_URL,
                            HomeMenuType.SOPT_FAQ_AUTHENTICATED_URL,
                        )
                }
            }

    private val _checkNewInPokeUiState = MutableStateFlow<UiState<CheckNewInPoke>>(UiState.Loading)
    val checkNewInPokeUiState: StateFlow<UiState<CheckNewInPoke>> get() = _checkNewInPokeUiState

    private val _appServiceState = MutableStateFlow<AppServiceState>(AppServiceState())
    val appServiceState: StateFlow<AppServiceState>
        get() = _appServiceState

    fun initHomeUi(userStatus: UserStatus) {
        viewModelScope.launch {
            if (userStatus != UserStatus.UNAUTHENTICATED) {
                homeRepository.getMainView()
                    .onSuccess {
                        homeUiState.value = it
                    }
                    .onFailure { error ->
                        if (error is HttpException && error.code() == 400) {
                            homeUiState.value =
                                SoptUser(
                                    User(activeState = UserActiveState.INACTIVE),
                                    SoptActiveRecord(),
                                    false,
                                )
                        }
                        Timber.e(error)
                    }
            } else {
                homeUiState.value =
                    SoptUser(
                        User(),
                        SoptActiveRecord(),
                    )
            }

            homeRepository.getAppService()
                .onSuccess { appServiceList ->
                    var appService = AppServiceState()

                    appServiceList.map { service ->
                        when (service.serviceName) {
                            AppServiceEnum.SOPTAMP.name -> {
                                appService = appService.copy(
                                    showSoptamp = (homeUiState.value.user.activeState == UserActiveState.ACTIVE && service.activeUser) || (homeUiState.value.user.activeState == UserActiveState.INACTIVE && service.inactiveUser)
                                )
                            }

                            AppServiceEnum.POKE.name -> {
                                appService = appService.copy(
                                    showPoke = (homeUiState.value.user.activeState == UserActiveState.ACTIVE && service.activeUser) || (homeUiState.value.user.activeState == UserActiveState.INACTIVE && service.inactiveUser)
                                )
                            }

                            else -> {}
                        }
                    }

                    _appServiceState.value = appService
                }
        }
    }

    fun initMainDescription(userStatus: UserStatus) {
        viewModelScope.launch {
            if (userStatus != UserStatus.UNAUTHENTICATED) {
                homeRepository.getMainDescription()
                    .onSuccess {
                        _description.value = it
                    }
            }
        }
    }

    fun registerPushToken(userStatus: UserStatus) {
        if (userStatus == UserStatus.UNAUTHENTICATED) return
        viewModelScope.launch {
            runCatching {
                FirebaseMessaging.getInstance().token.await()
            }.onSuccess {
                registerPushTokenUseCase(it)
                    .onFailure(Timber::e)
            }.onFailure(Timber::e)
        }
    }

    fun checkNewInPoke() {
        viewModelScope.launch {
            _checkNewInPokeUiState.emit(UiState.Loading)
            checkNewInPokeUseCase.invoke()
                .onSuccess { response ->
                    _checkNewInPokeUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _checkNewInPokeUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _checkNewInPokeUiState.emit(UiState.Failure(throwable))
                }
        }
    }
}
