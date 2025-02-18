package org.sopt.official.feature.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.soptlog.navigation.soptlogNavGraph

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier.background(color = SoptTheme.colors.background),
            ) {
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navigator.navController,
                    startDestination = navigator.startDestination
                ) {
                    dummyNavGraph(paddingValues = innerPadding)
                    soptlogNavGraph(paddingValues = innerPadding)
                }

                SoptBottomBar(
                    visible = navigator.shouldShowBottomBar(),
                    tabs = MainTab.entries.toPersistentList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = navigator::navigate,
                )
            }
        }
    )
}

@Composable
fun SoptBottomBar(
    visible: Boolean,
    tabs: ImmutableList<MainTab>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) }
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
