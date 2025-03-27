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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.auth.model.UserStatus.UNAUTHENTICATED
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.home.component.HomeEnjoySoptServicesBlock
import org.sopt.official.feature.home.component.HomeErrorDialog
import org.sopt.official.feature.home.component.HomeProgressIndicator
import org.sopt.official.feature.home.component.HomeShortcutButtonsForMember
import org.sopt.official.feature.home.component.HomeShortcutButtonsForVisitor
import org.sopt.official.feature.home.component.HomeSoptScheduleDashboard
import org.sopt.official.feature.home.component.HomeTopBarForMember
import org.sopt.official.feature.home.component.HomeTopBarForVisitor
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForMember
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForVisitor
import org.sopt.official.feature.home.model.HomeAppService
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeUiState.Member
import org.sopt.official.feature.home.model.HomeUiState.Unauthenticated
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import org.sopt.official.feature.home.navigation.HomeNavigation
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeAppServicesNavigation
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeDashboardNavigation
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeShortcutNavigation
import org.sopt.official.feature.home.navigation.HomeUrl

@Composable
internal fun HomeRoute(
    userStatus: UserStatus,
    homeNavigation: HomeNavigation,
    newHomeViewModel: NewHomeViewModel = hiltViewModel(),
) {
    val uiState by newHomeViewModel.uiState.collectAsStateWithLifecycle()
    val tracker = LocalTracker.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

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
        if (userStatus != UNAUTHENTICATED) newHomeViewModel.refreshAll()
    }

    LaunchedEffect(Unit) {
        tracker.track(
            name = "at36_apphome",
            type = EventType.VIEW
        )
    }

    when (val state = uiState) {
        is Unauthenticated -> {
            HomeScreenForVisitor(
                homeShortcutNavigation = homeNavigation as HomeShortcutNavigation,
                homeAppServicesNavigation = homeNavigation as HomeAppServicesNavigation,
                homeDashboardNavigation = homeNavigation as HomeDashboardNavigation,
                homeAppServices = uiState.homeServices,
            )
        }

        is Member -> {
            HomeScreenForMember(
                homeDashboardNavigation = homeNavigation as HomeDashboardNavigation,
                homeShortcutNavigation = homeNavigation as HomeShortcutNavigation,
                onAppServiceClick = { url, _ ->
                    val homeAppServicesNavigation = homeNavigation as HomeAppServicesNavigation

                    when (HomeUrl.from(url)) {
                        HomeUrl.POKE -> {
                            scope.launch {
                                newHomeViewModel.fetchIsNewPoke()
                                    .onSuccess { isNewPoke ->
                                        trackClickEvent(tracker, "at36_poke_menu")
                                        homeAppServicesNavigation.navigateToPoke(
                                            url = url,
                                            isNewPoke = isNewPoke,
                                            currentDestination = state.homeUserSoptLogDashboardModel.recentGeneration,
                                        )
                                    }
                            }
                        }

                        HomeUrl.FORTUNE -> {
                            homeAppServicesNavigation.navigateToDeepLink(url)
                            trackClickEvent(tracker, "at36_todaysoptmadi_menu")
                        }

                        HomeUrl.SOPTAMP -> {
                            homeAppServicesNavigation.navigateToDeepLink(url)
                            trackClickEvent(tracker, "at36_soptamp_menu")
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
                tracker = tracker
            )
        }
    }

    if (uiState.isLoading) HomeProgressIndicator()
    if (uiState.isError) HomeErrorDialog(onCheckClick = { newHomeViewModel.refreshAll() })
}

@Composable
private fun HomeScreenForMember(
    homeDashboardNavigation: HomeDashboardNavigation,
    homeShortcutNavigation: HomeShortcutNavigation,
    onAppServiceClick: (url: String, appServiceName: String) -> Unit,
    hasNotification: Boolean,
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    homeSoptScheduleModel: HomeSoptScheduleModel,
    homeAppServices: ImmutableList<HomeAppService>,
    tracker: Tracker
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))

        HomeTopBarForMember(
            hasNotification = hasNotification,
            onNotificationClick = {
                homeDashboardNavigation.navigateToNotification()
                trackClickEvent(tracker, "at36_alarm")
            },
            onSettingClick = homeDashboardNavigation::navigateToSetting,
        )

        Spacer(modifier = Modifier.height(height = 16.dp))

        HomeUserSoptLogDashboardForMember(
            onDashboardClick = homeDashboardNavigation::navigateToSoptlog,
            homeUserSoptLogDashboardModel = homeUserSoptLogDashboardModel,
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
            }
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
        )

        Spacer(modifier = Modifier.height(height = 40.dp))

        HomeEnjoySoptServicesBlock(
            appServices = homeAppServices,
            onAppServiceClick = onAppServiceClick,
        )
    }
}

@Composable
private fun HomeScreenForVisitor(
    homeShortcutNavigation: HomeShortcutNavigation,
    homeDashboardNavigation: HomeDashboardNavigation,
    homeAppServicesNavigation: HomeAppServicesNavigation,
    homeAppServices: ImmutableList<HomeAppService>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        HomeTopBarForVisitor(onSettingClick = homeDashboardNavigation::navigateToSetting)
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashboardForVisitor(onDashboardClick = homeDashboardNavigation::navigateToSoptlog)
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
