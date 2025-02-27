package org.sopt.official.feature.main

import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import dagger.hilt.android.EntryPointAccessors
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.auth.model.UserStatus.UNAUTHENTICATED
import org.sopt.official.common.context.appContext
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.common.navigator.NavigatorEntryPoint
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.model.HomeNavigation.HomeAppServicesNavigation
import org.sopt.official.feature.home.model.HomeNavigation.HomeDashboardNavigation
import org.sopt.official.feature.home.model.HomeNavigation.HomeShortcutNavigation
import org.sopt.official.feature.home.navigation.homeNavGraph
import org.sopt.official.feature.main.model.PlaygroundWebLink
import org.sopt.official.feature.main.model.SoptWebLink
import org.sopt.official.feature.soptlog.navigation.soptlogNavGraph
import org.sopt.official.webview.view.WebViewActivity
import org.sopt.official.webview.view.WebViewActivity.Companion.INTENT_URL

private val applicationNavigator by lazy {
    EntryPointAccessors.fromApplication(
        appContext,
        NavigatorEntryPoint::class.java
    ).navigatorProvider()
}

@Composable
fun MainScreen(
    userStatus: UserStatus,
    navigator: MainNavigator = rememberMainNavigator(),
) {
    val context = LocalContext.current
    var isOpenDialog by remember { mutableStateOf(false) }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = SoptTheme.colors.background)
                    .padding(innerPadding),
            ) {
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navigator.navController,
                    startDestination = navigator.startDestination
                ) {
                    homeNavGraph(
                        userStatus = userStatus,
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
                            override fun navigateToSoptlog() = navigator.navigateToSoptlog(userStatus)
                            override fun navigateToAttendance() = context.startActivity(applicationNavigator.getAttendanceActivityIntent())
                            override fun navigateToDeepLink(url: String) {
                                if (userStatus == UNAUTHENTICATED) isOpenDialog = true
                                else context.startActivity(DeepLinkType.of(url).getMainIntent(context, userStatus, url))
                            }
                        },
                    )

                    soptlogNavGraph(
                        navigateToEditProfile = {
                            val intent = Intent(context, WebViewActivity::class.java).apply {
                                putExtra(INTENT_URL, PlaygroundWebLink.EDIT_PROFILE)
                            }
                            context.startActivity(intent)
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
                    tabs = MainTab.entries.toPersistentList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = {
                        navigator.navigate(it, userStatus) {
                            isOpenDialog = true
                        }
                    },
                )
            }
        }
    )

    if (isOpenDialog) {
        UnauthenticatedDDialog(
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
fun UnauthenticatedDDialog(
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
    containerColor: Color = SoptTheme.colors.onSurface800,
    textColor: Color = SoptTheme.colors.primary,
    modifier: Modifier = Modifier,
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
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(color = SoptTheme.colors.onSurface800),
        ) {
            tabs.forEach { tab ->
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.contentDescription,
                    tint = if (tab == currentTab) {
                        SoptTheme.colors.primary
                    } else {
                        SoptTheme.colors.onSurface500
                    },
                    modifier = Modifier
                        .padding(vertical = 22.dp)
                        .size(24.dp)
                        .weight(1f)
                        .clickable { onTabSelected(tab) }
                )

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
            currentTab = MainTab.Home,
            onTabSelected = {},
        )
    }
}
