/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.util.successOr
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.model.FloatingToast
import org.sopt.official.domain.home.model.RecentCalendar
import org.sopt.official.domain.home.model.ReviewForm
import org.sopt.official.domain.home.model.UserInfo
import org.sopt.official.domain.home.model.UserStatus
import org.sopt.official.domain.home.model.UserStatus.ACTIVE
import org.sopt.official.domain.home.model.UserStatus.INACTIVE
import org.sopt.official.domain.home.model.UserStatus.UNAUTHENTICATED
import org.sopt.official.domain.home.repository.HomeRepository
import org.sopt.official.domain.home.usecase.GetAppServiceUseCase
import org.sopt.official.domain.notification.usecase.RegisterPushTokenUseCase
import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.CheckNewInPoke
import org.sopt.official.domain.poke.usecase.CheckNewInPokeUseCase
import org.sopt.official.feature.home.mapper.toModel
import org.sopt.official.feature.home.model.HomeAppService
import org.sopt.official.feature.home.model.HomeFloatingToastData
import org.sopt.official.feature.home.model.HomePlaygroundPostModel
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeSurveyData
import org.sopt.official.feature.home.model.HomeUiState
import org.sopt.official.feature.home.model.HomeUiState.ActiveMember
import org.sopt.official.feature.home.model.HomeUiState.InactiveMember
import org.sopt.official.feature.home.model.HomeUiState.Unauthenticated
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import org.sopt.official.feature.home.model.Schedule
import org.sopt.official.feature.home.model.defaultAppServices
import timber.log.Timber

@Inject
@ViewModelKey(NewHomeViewModel::class)
@ContributesIntoMap(AppScope::class)
internal class NewHomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val checkNewInPokeUseCase: CheckNewInPokeUseCase,
    private val registerPushTokenUseCase: RegisterPushTokenUseCase,
    private val getAppServiceUseCase: GetAppServiceUseCase
) : ViewModel() {
    private val viewModelState: MutableStateFlow<HomeViewModelState> =
        MutableStateFlow(HomeViewModelState())

    val uiState: StateFlow<HomeUiState> = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState(),
        )

    fun refreshAll() {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val userInfoDeferred = async { homeRepository.getUserInfo() }
            val userDescriptionDeferred = async { homeRepository.getHomeDescription() }
            val recentCalendarDeferred = async { homeRepository.getRecentCalendar() }
            val appServiceDeferred = async { getAppServiceUseCase() }
            val surveyDataDeferred = async { homeRepository.getHomeReviewForm() }
            val toastDataDeferred = async { homeRepository.getHomeFloatingToast() }
            val popularPostsDeferred = async { homeRepository.getHomePopularPosts() }
            val latestPostsDeferred = async { homeRepository.getHomeLatestPosts() }

            val userInfo = userInfoDeferred.await().successOr(UserInfo(UserInfo.User()))
            val userDescription =
                userDescriptionDeferred.await().successOr(UserInfo.UserDescription())
            val recentCalendar = recentCalendarDeferred.await().successOr(RecentCalendar())
            val appService = appServiceDeferred.await().successOr(emptyList())
            val surveyData = surveyDataDeferred.await().successOr(ReviewForm.default)
            val toastData = toastDataDeferred.await().successOr(FloatingToast.default)
            val popularPostsData = popularPostsDeferred.await().successOr(emptyList())
            val latestPostData = latestPostsDeferred.await().successOr(emptyList())

            if (userInfo.user.userStatus != UNAUTHENTICATED) {
                Timber.d("사용자 상태가 인증됨: ${userInfo.user.userStatus}, FCM 토큰 등록 시작")
                registerFcmToken()
            } else {
                Timber.d("사용자 상태가 인증되지 않음: UNAUTHENTICATED, FCM 토큰 등록 건너뜀")
            }

            viewModelState.update {
                it.copy(
                    isError = userInfo.user.name.isBlank() || userDescription.activityDescription.isBlank(), // 반복 에러 대응 필요
                    isLoading = false,
                    userState = userInfo.user.userStatus,
                    userInfo = userInfo,
                    userDescription = userDescription,
                    recentCalendar = recentCalendar,
                    appServices = appService,
                    surveyData = surveyData.toModel(),
                    toastData = toastData.toModel(),
                    popularPostData = popularPostsData.map { post -> post.toModel() }.toImmutableList(),
                    latestPostData = latestPostData.map { post -> post.toModel() }.toImmutableList()
                )
            }
        }
    }

    private fun registerFcmToken() {
        viewModelScope.launch {
            Timber.d("FCM 토큰 가져오기 시작")
            runCatching {
                FirebaseMessaging.getInstance().token.await()
            }.onSuccess { token ->
                Timber.d("FCM 토큰 가져오기 성공: $token")
                runCatching {
                    registerPushTokenUseCase(token)
                }.onSuccess {
                    Timber.d("FCM 토큰 서버 등록 성공: $token")
                }.onFailure { error ->
                    Timber.e(error, "FCM 토큰 서버 등록 실패: $token")
                }
            }.onFailure { error ->
                Timber.e(error, "FCM 토큰 가져오기 실패")
            }
        }
    }

    suspend fun fetchIsNewPoke(): Result<Boolean> {
        viewModelState.update { it.copy(isLoading = true) }

        return when (val apiResult: ApiResult<*> = checkNewInPokeUseCase()) {
            is ApiResult.Success -> {
                viewModelState.update { it.copy(isLoading = false) }
                Result.success((apiResult as ApiResult.Success<CheckNewInPoke>).data.isNew)
            }

            is ApiResult.ApiError -> {
                viewModelState.update { it.copy(isLoading = false) }
                Result.failure(Exception("API Error: ${apiResult.statusCode} - ${apiResult.responseMessage}"))
            }

            is ApiResult.Failure -> {
                viewModelState.update { it.copy(isLoading = false) }
                Result.failure(apiResult.throwable)
            }
        }
    }

    fun refreshNotificationStatus() {
        viewModelScope.launch {
            val result = homeRepository.getUserInfo()
            val userInfo = result.successOr(UserInfo(UserInfo.User()))
            viewModelState.update { state ->
                state.copy(
                    userInfo = userInfo
                )
            }
        }
    }
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val userState: UserStatus = UNAUTHENTICATED,
    val userInfo: UserInfo = UserInfo(),
    val userDescription: UserInfo.UserDescription = UserInfo.UserDescription(),
    val recentCalendar: RecentCalendar = RecentCalendar(),
    val appServices: List<AppService> = emptyList(),
    val surveyData: HomeSurveyData = HomeSurveyData(),
    val toastData: HomeFloatingToastData = HomeFloatingToastData(),
    val popularPostData: ImmutableList<HomePlaygroundPostModel> = persistentListOf(),
    val latestPostData: ImmutableList<HomePlaygroundPostModel> = persistentListOf()
) {
    fun toUiState(): HomeUiState = when (userState) {
        UNAUTHENTICATED -> Unauthenticated(
            isLoading = isLoading,
            isError = isError,
            homeServices = defaultAppServices,
            surveyData = surveyData,
            floatingToastData = toastData
        )

        ACTIVE -> ActiveMember(
            isLoading = isLoading,
            isError = isError,
            hasNotification = userInfo.isAllConfirm.not(),
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(
                userProfile = userInfo.user.profileImage,
                activityDescription = userDescription.activityDescription,
                generations = userInfo.user.generationList.toImmutableList(),
                isActivated = true,
            ),
            homeSoptScheduleModel = HomeSoptScheduleModel(
                type = Schedule.from(recentCalendar.scheduleType),
                date = recentCalendar.date,
                title = recentCalendar.title,
            ),
            homeServices = appServices.map {
                HomeAppService(
                    serviceName = it.serviceName,
                    isShowAlarmBadge = it.displayAlarmBadge,
                    alarmBadgeContent = it.alarmBadge,
                    iconUrl = it.iconUrl,
                    deepLink = it.deepLink,
                )
            }.toImmutableList(),
            surveyData = surveyData,
            floatingToastData = toastData,
            popularPosts = popularPostData,
            latestPosts = latestPostData,
        )

        INACTIVE -> InactiveMember(
            isLoading = isLoading,
            isError = isError,
            hasNotification = userInfo.isAllConfirm.not(),
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(
                userProfile = userInfo.user.profileImage,
                activityDescription = userDescription.activityDescription,
                generations = userInfo.user.generationList.toImmutableList(),
                isActivated = false,
            ),
            homeSoptScheduleModel = HomeSoptScheduleModel(
                type = Schedule.from(recentCalendar.scheduleType),
                date = recentCalendar.date,
                title = recentCalendar.title,
            ),
            homeServices = appServices.map {
                HomeAppService(
                    serviceName = it.serviceName,
                    isShowAlarmBadge = it.displayAlarmBadge,
                    alarmBadgeContent = it.alarmBadge,
                    iconUrl = it.iconUrl,
                    deepLink = it.deepLink,
                )
            }.toImmutableList(),
            surveyData = surveyData,
            floatingToastData = toastData,
            popularPosts = popularPostData,
            latestPosts = latestPostData,
        )
    }
}