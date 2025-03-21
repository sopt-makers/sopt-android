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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
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

@Composable
internal fun HomeRoute(
    userStatus: UserStatus,
    homeNavigation: HomeNavigation,
    newHomeViewModel: NewHomeViewModel = hiltViewModel(),
) {
    val uiState by newHomeViewModel.uiState.collectAsStateWithLifecycle()
    val amplitudeTracker = LocalTracker.current

    LaunchedEffect(userStatus) {
        if (userStatus != UNAUTHENTICATED) newHomeViewModel.refreshAll()
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

                    when (url) {
                        "home/poke" -> {
                            amplitudeTracker.track(
                                name = "click_poke_menu",
                                type = EventType.CLICK
                            )
                            newHomeViewModel.fetchIsNewPoke(
                                onComplete = { isNewPoke ->
                                    homeAppServicesNavigation.navigateToPoke(
                                        url = url,
                                        isNewPoke = isNewPoke,
                                        currentDestination = state.homeUserSoptLogDashboardModel.recentGeneration,
                                    )
                                }
                            )
                        }

                        "home/fortune" -> {
                            homeAppServicesNavigation.navigateToDeepLink(url)
                            amplitudeTracker.track(
                                name = "click_todaysoptmadi_menu",
                                type = EventType.CLICK
                            )
                        }

                        "home/soptamp" -> {
                            homeAppServicesNavigation.navigateToDeepLink(url)
                            amplitudeTracker.track(
                                name = "click_soptamp_menu",
                                type = EventType.CLICK
                            )
                        }

                        else -> homeAppServicesNavigation.navigateToDeepLink(url)
                    }
                },
                hasNotification = state.hasNotification,
                homeUserSoptLogDashboardModel = state.homeUserSoptLogDashboardModel,
                homeSoptScheduleModel = state.homeSoptScheduleModel,
                homeAppServices = uiState.homeServices,
                amplitudeTracker = amplitudeTracker
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
    amplitudeTracker: Tracker
) {

    LaunchedEffect(true) {
        amplitudeTracker.track(
            name = "view_apphome",
            type = EventType.VIEW
        )
    }

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
                amplitudeTracker.track(
                    name = "click_alarm",
                    type = EventType.CLICK
                )
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
                amplitudeTracker.track(
                    name = "click_all_calendar",
                    type = EventType.CLICK
                )
            },
            onAttendanceButtonClick = {
                homeDashboardNavigation.navigateToAttendance()
                amplitudeTracker.track(
                    name = "click_attendance",
                    type = EventType.CLICK
                )
            }
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        HomeShortcutButtonsForMember(
            onPlaygroundClick = {
                homeShortcutNavigation.navigateToPlayground()
                amplitudeTracker.track(
                    name = "click_playground_community",
                    type = EventType.CLICK
                )
            },
            onStudyClick = {
                homeShortcutNavigation.navigateToPlaygroundGroup()
                amplitudeTracker.track(
                    name = "click_moim",
                    type = EventType.CLICK
                )
            },
            onMemberClick = {
                homeShortcutNavigation.navigateToPlaygroundMember()
                amplitudeTracker.track(
                    name = "click_member",
                    type = EventType.CLICK
                )
            },
            onProjectClick = {
                homeShortcutNavigation.navigateToPlaygroundProject()
                amplitudeTracker.track(
                    name = "click_project",
                    type = EventType.CLICK
                )
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
