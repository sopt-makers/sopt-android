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
package org.sopt.official.feature.main

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.zacsweers.metro.viewmodel.compose.metroViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.common.view.toast
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.navigation.appjamtampNavGraph
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeAppServicesNavigation
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeDashboardNavigation
import org.sopt.official.feature.home.navigation.HomeNavigation.HomeShortcutNavigation
import org.sopt.official.feature.home.navigation.homeNavGraph
import org.sopt.official.feature.main.MainTab.Home
import org.sopt.official.feature.main.component.MainBottomBarAlarmBadge
import org.sopt.official.feature.main.model.PlaygroundWebLink
import org.sopt.official.feature.main.model.SoptWebLink
import org.sopt.official.feature.poke.navigation.navigateToPokeFriendList
import org.sopt.official.feature.poke.navigation.navigateToPokeNotification
import org.sopt.official.feature.poke.navigation.navigateToPokeOnboarding
import org.sopt.official.feature.poke.navigation.pokeNavGraph
import org.sopt.official.feature.soptlog.navigation.SoptLogNavigation
import org.sopt.official.feature.soptlog.navigation.soptLogNavGraph
import org.sopt.official.model.UserStatus
import org.sopt.official.stamp.feature.navigation.soptampNavGraph
import org.sopt.official.webview.view.WebViewActivity
import org.sopt.official.webview.view.WebViewActivity.Companion.INTENT_URL

private const val EXIT_MILLIS = 3000L

@Composable
fun MainScreen(
    userStatus: UserStatus,
    applicationNavigator: NavigatorProvider,
    intentState: Intent?,
    navigator: MainNavigator = rememberMainNavigator(),
    viewModel: MainViewModel = metroViewModel()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val tracker = LocalTracker.current
    var isOpenDialog by remember { mutableStateOf(false) }

    val visibleTabs by viewModel.mainTabs.collectAsStateWithLifecycle()
    val badgeList by viewModel.badgeMap.collectAsStateWithLifecycle()

    var backPressedTime = 0L

    BackHandler {
        if (System.currentTimeMillis() - backPressedTime <= EXIT_MILLIS) {
            activity?.finish()
        } else {
            activity?.toast("한번 더 누르면 앱을 종료할 수 있어요")
        }
        backPressedTime = System.currentTimeMillis()
    }

    // soptamp 검사 로직
    val shouldNavigateToSoptamp = {
        val intent = intentState
        val hasSoptampFlag = intent?.getBooleanExtra("isSoptampDeepLink", false) == true
        val hasMissionArgs = intent?.hasExtra("soptampArgs") == true
        hasSoptampFlag || hasMissionArgs
    }

    val shouldNavigateToAppjamtamp = {
        val intent = intentState
        val hasAppjamtampFlag = intent?.getBooleanExtra("isAppjamtampDeepLink", false) == true
        val hasMissionArgs = intent?.hasExtra("appjamtampArgs") == true
        hasAppjamtampFlag || hasMissionArgs
    }

    // soptLog 검사 로직
    val shouldNavigateToSoptLog = remember(intentState) {
        val intent = intentState
        intent?.getBooleanExtra("isSoptLogDeepLink", false) == true
    }

    // poke 검사 로직
    val shouldNavigateToPoke = remember(intentState) {
        val intent = intentState
        intent?.getBooleanExtra("isPokeDeepLink", false) == true
    }

    val shouldNavigateToPokeNotification = remember(intentState) {
        val intent = intentState
        intent?.getBooleanExtra("isPokeNotification", false) == true
    }

    val shouldNavigatePokeFriendList = remember(intentState) {
        val intent = intentState
        intent?.getBooleanExtra("isPokeFriendList", false) == true
        intent?.hasExtra("friendType") == true
    }

    LaunchedEffect(
        shouldNavigateToSoptamp,
        shouldNavigateToAppjamtamp,
        shouldNavigateToPoke,
        shouldNavigateToPokeNotification,
        shouldNavigatePokeFriendList,
        shouldNavigateToSoptLog
    ) {
        if (shouldNavigateToSoptamp()) {
            navigator.navigateAndClear(MainTab.Soptamp, userStatus)
            activity?.intent?.putExtra("isSoptampDeepLink", false)
        }

        if (shouldNavigateToAppjamtamp()) {
            navigator.navigateAndClear(MainTab.Appjamtamp, userStatus)
            activity?.intent?.putExtra("isSoptampDeepLink", false)
        }

        if (shouldNavigateToSoptLog) {
            navigator.navigate(MainTab.SoptLog, userStatus)
            activity?.intent?.putExtra("isSoptLogDeepLink", false)
        }

        if (shouldNavigateToPoke) {
            navigator.navigate(MainTab.Poke, userStatus)
            activity?.intent?.putExtra("isPokeDeepLink", false)
        }

        if (shouldNavigateToPokeNotification) {
            navigator.navController.navigateToPokeNotification(userStatus.name, null)
            activity?.intent?.putExtra("isPokeNotification", false)
        }

        if (shouldNavigatePokeFriendList) {
            navigator.navController.navigateToPokeFriendList(
                activity?.intent?.getStringExtra("friendType"),
                null
            )
            activity?.intent?.putExtra("isPokeFriendList", false)
        }
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = SoptTheme.colors.background),
            ) {
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navigator.navController,
                    startDestination = navigator.startDestination
                ) {
                    homeNavGraph(
                        userStatus = userStatus,
                        paddingValues = innerPadding,
                        onUpdateBottomBadge = viewModel::updateBadge,
                        homeNavigation = object : HomeShortcutNavigation, HomeDashboardNavigation, HomeAppServicesNavigation {
                            private fun getIntent(url: String) = Intent(context, WebViewActivity::class.java).apply {
                                putExtra(INTENT_URL, url)
                            }

                            override fun navigateToPlayground() = context.startActivity(getIntent(PlaygroundWebLink.OFFICIAL_HOMEPAGE))
                            override fun navigateToPlaygroundGroup() = context.startActivity(getIntent(PlaygroundWebLink.GROUP_STUDY))
                            override fun navigateToPlaygroundMember() = context.startActivity(getIntent(PlaygroundWebLink.MEMBER))
                            override fun navigateToPlaygroundProject() = context.startActivity(getIntent(PlaygroundWebLink.PROJECT))
                            override fun navigateToSoptHomepage() = context.startActivity(getIntent(SoptWebLink.OFFICIAL_HOMEPAGE))
                            override fun navigateToSoptReview() = context.startActivity(getIntent(SoptWebLink.REVIEW))
                            override fun navigateToSoptProject() = context.startActivity(getIntent(SoptWebLink.PROJECT))
                            override fun navigateToSoptInstagram() = context.startActivity(getIntent(SoptWebLink.INSTAGRAM))

                            override fun navigateToNotification() =
                                context.startActivity(applicationNavigator.getNotificationActivityIntent())

                            override fun navigateToSetting() =
                                context.startActivity(applicationNavigator.getMyPageActivityIntent(userStatus.name))

                            override fun navigateToSchedule() = context.startActivity(applicationNavigator.getScheduleActivityIntent())
                            override fun navigateToEditProfile() {
                                val intent = Intent(context, WebViewActivity::class.java).apply {
                                    putExtra(INTENT_URL, PlaygroundWebLink.EDIT_PROFILE)
                                }
                                context.startActivity(intent)
                            }

                            override fun navigateToAttendance() = context.startActivity(applicationNavigator.getAttendanceActivityIntent())
                            override fun navigateToDeepLink(url: String) {
                                if (userStatus == UserStatus.UNAUTHENTICATED) {
                                    isOpenDialog = true
                                    return
                                }

                                val deepLinkType = DeepLinkType.of(url)

                                when (deepLinkType) {
                                    DeepLinkType.SOPTAMP -> {
                                        navigator.navigate(MainTab.Soptamp, userStatus)
                                    }

                                    DeepLinkType.APPJAMTAMP -> {
                                        navigator.navigate(MainTab.Appjamtamp, userStatus)
                                    }

                                    else -> {
                                        context.startActivity(deepLinkType.getIntent(context, userStatus, url))
                                    }
                                }
                            }

                            override fun navigateToWebUrl(url: String) {
                                context.startActivity(getIntent(url))
                            }

                            override fun navigateToPoke(url: String, isNewPoke: Boolean, currentDestination: Int) =
                                when (isNewPoke) {
                                    true -> navigator.navController.navigateToPokeOnboarding(
                                        generation = currentDestination,
                                        userStatus = userStatus.name
                                    )

                                    false -> navigator.navigate(MainTab.Poke, userStatus)
                                }

                            override fun navigateToPlaygroundMemberProfile(userId: Int) {
                                context.startActivity(
                                    getIntent("${PlaygroundWebLink.MEMBER}/$userId")
                                )
                            }
                        }
                    )

                    soptampNavGraph(
                        navController = navigator.navController,
                        tracker = tracker,
                        currentIntent = intentState
                    )

                    appjamtampNavGraph(
                        paddingValues = innerPadding,
                        navController = navigator.navController
                    )

                    pokeNavGraph(
                        navController = navigator.navController,
                        paddingValues = innerPadding,
                        userStatus = userStatus
                    )

                    soptLogNavGraph(
                        soptLogNavigation = object : SoptLogNavigation {
                            override fun navigateToDeepLink(url: String) {
                                if (userStatus == UserStatus.UNAUTHENTICATED) isOpenDialog = true
                                else if (url == DeepLinkType.SOPTAMP.link) {
                                    navigator.navigate(MainTab.Soptamp, userStatus)
                                } else {
                                    context.startActivity(DeepLinkType.of(url).getIntent(context, userStatus, url))
                                }
                            }

                            override fun navigateToPoke(url: String, isNewPoke: Boolean, currentDestination: Int, friendType: String?) {
                                when {
                                    url.contains("home/poke/friend-list-summary") -> {
                                        navigator.navController.navigateToPokeFriendList(friendType, null)
                                    }

                                    isNewPoke -> {
                                        navigator.navController.navigateToPokeOnboarding(currentDestination, userStatus.name)
                                    }

                                    else -> {
                                        navigator.navigate(MainTab.Poke, userStatus)
                                    }
                                }
                            }
                        },
                        navigateToFortune = {
                            context.startActivity(
                                applicationNavigator.getFortuneActivityIntent()
                            )
                        }
                    )
                }

                SoptBottomBar(
                    visible = navigator.shouldShowBottomBar(),
                    tabs = visibleTabs.toImmutableList(),
                    showBadgeContent = badgeList,
                    currentTab = navigator.currentTab,
                    onTabSelected = { selectedTab ->
                        if (selectedTab.loggingName != null) {
                            tracker.track(
                                name = selectedTab.loggingName,
                                type = EventType.CLICK,
                                properties = mapOf("view_type" to userStatus.value)
                            )
                        }

                        if (navigator.isSameTab(selectedTab)) {
                            navigator.navigateAndClear(selectedTab, userStatus) {
                                isOpenDialog = true
                            }
                        } else {
                            navigator.navigate(selectedTab, userStatus) {
                                isOpenDialog = true
                            }
                        }
                    },
                )
            }

            SlideUpDownWithFadeAnimatedVisibility(
                visible = navigator.currentTab == MainTab.Home || navigator.currentTab == MainTab.SoptLog
            ) {
                MainFloatingButton(
                    paddingValues = innerPadding
                )
            }
        }
    )

    if (isOpenDialog) {
        UnauthenticatedDialog(
            onDismissRequest = { isOpenDialog = false },
            onLogin = {
                context.startActivity(
                    applicationNavigator.getAuthActivityIntent()
                )
                isOpenDialog = false
            }
        )
    }
}

@Composable
fun UnauthenticatedDialog(
    onDismissRequest: () -> Unit,
    onLogin: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = SoptTheme.colors.onSurface800
            ),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "로그인이 필요해요",
                    style = SoptTheme.typography.title18SB,
                    color = SoptTheme.colors.onSurface10,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "솝트로그는 로그인을 해야만 사용할 수 있어요.\n" + "솝트 회원이라면 로그인해주세요.",
                    style = SoptTheme.typography.body14R,
                    color = SoptTheme.colors.onSurface100,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    SoptButton(
                        title = "닫기",
                        onClick = onDismissRequest,
                        containerColor = SoptTheme.colors.onSurface600,
                        textColor = SoptTheme.colors.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SoptButton(
                        title = "로그인",
                        onClick = onLogin,
                        containerColor = SoptTheme.colors.primary,
                        textColor = SoptTheme.colors.onPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SoptButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = SoptTheme.colors.onSurface800,
    textColor: Color = SoptTheme.colors.primary,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = SoptTheme.typography.label16SB,
            color = textColor,
        )
    }
}

@Composable
fun SoptBottomBar(
    visible: Boolean,
    tabs: ImmutableList<MainTab>,
    showBadgeContent: Map<MainTab, String?>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SlideUpDownWithFadeAnimatedVisibility(
        visible = visible,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = SoptTheme.colors.onSurface800),
        ) {
            tabs.forEach { tab ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .weight(1f)
                        .clickable { onTabSelected(tab) }
                ) {
                    BadgedBox(
                        badge = {
                            showBadgeContent.forEach { badge ->
                                if (tab == badge.key && !badge.value.isNullOrBlank()) {
                                    MainBottomBarAlarmBadge(
                                        text = badge.value!!,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(tab.icon),
                            contentDescription = tab.contentDescription,
                            tint = if (tab == currentTab) {
                                SoptTheme.colors.primary
                            } else {
                                SoptTheme.colors.onSurface500
                            },
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                    Text(
                        text = tab.contentDescription,
                        style = SoptTheme.typography.body10M,
                        color = if (tab == currentTab) {
                            SoptTheme.colors.primary
                        } else {
                            SoptTheme.colors.onSurface500
                        },
                        modifier = Modifier
                            .padding(top = 2.dp)
                    )
                }

            }
        }
    }
}

@Composable
fun SlideUpDownWithFadeAnimatedVisibility(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) },
        content = content
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun MainScreenPreview() {
    SoptTheme {
        SoptBottomBar(
            visible = true,
            tabs = MainTab.entries.toPersistentList(),
            currentTab = Home,
            onTabSelected = {},
            showBadgeContent = emptyMap()
        )
    }
}
