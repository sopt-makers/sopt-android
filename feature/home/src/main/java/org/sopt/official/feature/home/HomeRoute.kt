/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zacsweers.metro.viewmodel.compose.metroViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.util.ui.dropShadow
import org.sopt.official.designsystem.GrayAlpha700
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.designsystem.component.indicator.LoadingIndicator
import org.sopt.official.feature.home.component.HomeEnjoySoptServicesBlock
import org.sopt.official.feature.home.component.HomeErrorDialog
import org.sopt.official.feature.home.component.HomeFloatingButton
import org.sopt.official.feature.home.component.HomeLatestNewsSection
import org.sopt.official.feature.home.component.HomeOfficialChannelButton
import org.sopt.official.feature.home.component.HomePopularNewsSection
import org.sopt.official.feature.home.component.HomeShortcutButtonsForMember
import org.sopt.official.feature.home.component.HomeShortcutButtonsForVisitor
import org.sopt.official.feature.home.component.HomeSoptScheduleDashboard
import org.sopt.official.feature.home.component.HomeSurveySection
import org.sopt.official.feature.home.component.HomeToastButton
import org.sopt.official.feature.home.component.HomeTopBarForMember
import org.sopt.official.feature.home.component.HomeTopBarForVisitor
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForMember
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForVisitor
import org.sopt.official.feature.home.model.HomeAppService
import org.sopt.official.feature.home.model.HomeFloatingToastData
import org.sopt.official.feature.home.model.HomePlaygroundPostModel
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeSurveyData
import org.sopt.official.feature.home.model.HomeUiState.Member
import org.sopt.official.feature.home.model.HomeUiState.Unauthenticated
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import org.sopt.official.feature.home.navigation.HomeNavigation
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeAppServicesNavigation
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeDashboardNavigation
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeShortcutNavigation
import org.sopt.official.feature.home.navigation.HomeUrl
import org.sopt.official.model.UserStatus

@Composable
internal fun HomeRoute(
    paddingValues: PaddingValues,
    userStatus: UserStatus,
    homeNavigation: HomeNavigation,
    onUpdateBottomBadge: (Map<String, String>) -> Unit,
    newHomeViewModel: NewHomeViewModel = metroViewModel(),
) {
    val uiState by newHomeViewModel.uiState.collectAsStateWithLifecycle()
    val tracker = LocalTracker.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    val badgeContentList = remember(uiState) {
        when (val state = uiState) {
            is Member -> {
                state.homeServices
                    .filter { it.isShowAlarmBadge }
                    .associate { it.deepLink to it.alarmBadgeContent }
            }

            else -> emptyMap()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                newHomeViewModel.refreshNotificationStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(userStatus) {
        if (userStatus != UserStatus.UNAUTHENTICATED) newHomeViewModel.refreshAll()
    }

    LaunchedEffect(Unit) {
        tracker.track(
            name = "at36_apphome",
            type = EventType.VIEW
        )
    }

    LaunchedEffect(badgeContentList) {
        onUpdateBottomBadge(badgeContentList)
    }

    when (val state = uiState) {
        is Unauthenticated -> {
            HomeScreenForVisitor(
                homeShortcutNavigation = homeNavigation as HomeShortcutNavigation,
                homeAppServicesNavigation = homeNavigation as HomeAppServicesNavigation,
                homeDashboardNavigation = homeNavigation as HomeDashboardNavigation,
                homeAppServices = uiState.homeServices,
                paddingValues = paddingValues
            )
        }

        is Member -> {
            HomeScreenForMember(
                homeDashboardNavigation = homeNavigation as HomeDashboardNavigation,
                homeShortcutNavigation = homeNavigation as HomeShortcutNavigation,
                homeAppServicesNavigation = homeNavigation as HomeAppServicesNavigation,
                onAppServiceClick = { url, _ ->
                    val homeAppServicesNavigation = homeNavigation as HomeAppServicesNavigation

                    when (HomeUrl.from(url)) {
                        HomeUrl.POKE -> {
                            scope.launch {
                                newHomeViewModel.fetchIsNewPoke()
                                    .onSuccess { isNewPoke ->
                                        trackClickEvent(tracker, "poke_menu")
                                        homeAppServicesNavigation.navigateToPoke(
                                            url = url,
                                            isNewPoke = isNewPoke,
                                            currentDestination = state.homeUserSoptLogDashboardModel.recentGeneration,
                                        )
                                    }
                            }
                        }

                        HomeUrl.SOPTAMP -> {
                            homeAppServicesNavigation.navigateToDeepLink(url)
                            trackClickEvent(tracker, "soptamp_menu")
                        }

                        HomeUrl.UNKNOWN -> {
                            if (isValidUrl(url)) {
                                homeAppServicesNavigation.navigateToDeepLink(url)
                            }
                        }
                    }
                },
                hasNotification = state.hasNotification,
                homeUserSoptLogDashboardModel = state.homeUserSoptLogDashboardModel,
                homeSoptScheduleModel = state.homeSoptScheduleModel,
                homeAppServices = uiState.homeServices,
                tracker = tracker,
                paddingValues = paddingValues,
                surveyData = state.surveyData,
                toastData = state.floatingToastData,
                popularPosts = state.popularPosts,
                latestPosts = state.latestPosts
            )
        }
    }

    if (uiState.isLoading) LoadingIndicator()
    if (uiState.isError) HomeErrorDialog(onCheckClick = { newHomeViewModel.refreshAll() })
}

@Composable
private fun HomeScreenForMember(
    homeDashboardNavigation: HomeDashboardNavigation,
    homeShortcutNavigation: HomeShortcutNavigation,
    homeAppServicesNavigation: HomeAppServicesNavigation,
    onAppServiceClick: (url: String, appServiceName: String) -> Unit,
    hasNotification: Boolean,
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    homeSoptScheduleModel: HomeSoptScheduleModel,
    homeAppServices: ImmutableList<HomeAppService>,
    tracker: Tracker,
    paddingValues: PaddingValues,
    surveyData: HomeSurveyData,
    toastData: HomeFloatingToastData,
    popularPosts: ImmutableList<HomePlaygroundPostModel>,
    latestPosts: ImmutableList<HomePlaygroundPostModel>
) {
    Box {
        val scrollState = rememberScrollState()
        val isScrolledBeyondThreshold by remember {
            derivedStateOf { scrollState.value > 130 || scrollState.isScrollInProgress }
        }
        val shadowModifier = Modifier.dropShadow(
            shape = CircleShape,
            color = GrayAlpha700,
            blur = 40.dp,
            offsetX = 0.dp,
            offsetY = 4.dp,
            spread = 0.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.height(height = 8.dp))

            HomeTopBarForMember(
                hasNotification = hasNotification,
                onNotificationClick = {
                    homeDashboardNavigation.navigateToNotification()
                    trackClickEvent(tracker, "at36_alarm")
                },
                onSettingClick = homeDashboardNavigation::navigateToSetting,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            HomeUserSoptLogDashboardForMember(
                onDashboardClick = homeDashboardNavigation::navigateToEditProfile,
                homeUserSoptLogDashboardModel = homeUserSoptLogDashboardModel,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(height = 12.dp))

            HomeSoptScheduleDashboard(
                homeSoptScheduleModel = homeSoptScheduleModel,
                isActivatedGeneration = homeUserSoptLogDashboardModel.isActivated,
                onScheduleClick = {
                    homeDashboardNavigation.navigateToSchedule()
                    trackClickEvent(tracker, "at36_all_calendar")
                },
                onAttendanceButtonClick = {
                    homeDashboardNavigation.navigateToAttendance()
                    trackClickEvent(tracker, "at36_attendance")
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(height = 12.dp))

            HomeShortcutButtonsForMember(
                onPlaygroundClick = {
                    homeShortcutNavigation.navigateToPlayground()
                    trackClickEvent(tracker, "at36_playground_community")
                },
                onStudyClick = {
                    homeShortcutNavigation.navigateToPlaygroundGroup()
                    trackClickEvent(tracker, "at36_moim")
                },
                onMemberClick = {
                    homeShortcutNavigation.navigateToPlaygroundMember()
                    trackClickEvent(tracker, "at36_member")
                },
                onProjectClick = {
                    homeShortcutNavigation.navigateToPlaygroundProject()
                    trackClickEvent(tracker, "at36_project")
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            if (homeAppServices.isNotEmpty()) {
                Spacer(modifier = Modifier.height(height = 40.dp))

                HomeEnjoySoptServicesBlock(
                    appServices = homeAppServices,
                    onAppServiceClick = onAppServiceClick,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }

            if (popularPosts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(height = 56.dp))

                HomePopularNewsSection(
                    postList = popularPosts,
                    navigateToWebLink = homeAppServicesNavigation::navigateToWebUrl,
                    navigateToMemberProfile = homeAppServicesNavigation::navigateToPlaygroundMemberProfile,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            if (latestPosts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(height = 56.dp))

                HomeLatestNewsSection(
                    feedList = latestPosts,
                    navigateToPlayground = homeShortcutNavigation::navigateToPlayground,
                    navigateToWebLink = homeAppServicesNavigation::navigateToWebUrl,
                    navigateToMemberProfile = homeAppServicesNavigation::navigateToPlaygroundMemberProfile
                )
            }

            if (surveyData.isActive) {
                Spacer(modifier = Modifier.height(height = 56.dp))

                HomeSurveySection(
                    surveyTitle = surveyData.title,
                    surveyDescription = surveyData.description,
                    buttonText = surveyData.buttonText,
                    onClick = {
                        homeAppServicesNavigation.navigateToWebUrl(surveyData.surveyLink)
                        trackClickEvent(tracker, "at36_survey_button")
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(height = 70.dp))

            HomeOfficialChannelButton(
                navigateToWebUrl = homeAppServicesNavigation::navigateToWebUrl,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(height = 58.dp))
        }

        if (toastData.active) {
            AnimatedHomeButton(
                visible = !isScrolledBeyondThreshold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 82.dp)
            ) {
                HomeToastButton(
                    imageUrl = toastData.imageUrl,
                    longTitle = toastData.title,
                    missionDescription = toastData.toastDescription,
                    buttonText = toastData.buttonText,
                    onClick = {
                        homeAppServicesNavigation.navigateToDeepLink(toastData.linkUrl)
                        trackClickEvent(tracker, "at36_toast_button")
                    },
                    modifier = shadowModifier
                )
            }

            AnimatedHomeButton(
                visible = isScrolledBeyondThreshold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 82.dp)
            ) {
                HomeFloatingButton(
                    imageUrl = toastData.imageUrl,
                    shortTitle = toastData.buttonDescription,
                    buttonText = toastData.buttonText,
                    onClick = {
                        homeAppServicesNavigation.navigateToDeepLink(toastData.linkUrl)
                        trackClickEvent(tracker, "at36_floating_button")
                    },
                    modifier = shadowModifier
                )
            }
        }
    }
}

@Composable
private fun HomeScreenForVisitor(
    homeShortcutNavigation: HomeShortcutNavigation,
    homeDashboardNavigation: HomeDashboardNavigation,
    homeAppServicesNavigation: HomeAppServicesNavigation,
    homeAppServices: ImmutableList<HomeAppService>,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        HomeTopBarForVisitor(onSettingClick = homeDashboardNavigation::navigateToSetting)
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashboardForVisitor(onDashboardClick = homeDashboardNavigation::navigateToEditProfile)
        Spacer(modifier = Modifier.height(height = 36.dp))
        Text(
            text = "SOPT를 더 알고 싶다면, 둘러보세요",
            style = typography.heading20B,
            color = colors.onBackground,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeShortcutButtonsForVisitor(
            onHomePageClick = homeShortcutNavigation::navigateToSoptHomepage,
            onPlaygroundClick = homeShortcutNavigation::navigateToSoptReview,
            onProjectClick = homeShortcutNavigation::navigateToSoptProject,
            onInstagramClick = homeShortcutNavigation::navigateToSoptInstagram,
        )
        Spacer(modifier = Modifier.height(height = 40.dp))
        HomeEnjoySoptServicesBlock(
            appServices = homeAppServices,
            onAppServiceClick = { url, _ -> homeAppServicesNavigation.navigateToDeepLink(url) },
        )
    }
}

@Composable
private fun AnimatedHomeButton(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { it * 3 },
            animationSpec = tween(durationMillis = 100)
        ),
        modifier = modifier
    ) {
        content()
    }
}
