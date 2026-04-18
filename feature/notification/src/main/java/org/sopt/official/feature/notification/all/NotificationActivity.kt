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
package org.sopt.official.feature.notification.all

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import org.sopt.official.common.navigator.NavigatorEntryPoint
import org.sopt.official.common.view.toast
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.designsystem.component.indicator.LoadingIndicator
import org.sopt.official.feature.notification.R
import org.sopt.official.feature.notification.all.component.NotificationCategoryChip
import org.sopt.official.feature.notification.all.component.NotificationInfoItem

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private val viewModel by viewModels<NotificationViewModel>()

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val notifications = viewModel.notifications.collectAsLazyPagingItems()
            var isPullRefreshing by remember { mutableStateOf(false) }

            val refreshLoadState = notifications.loadState.refresh
            val refreshState = rememberPullRefreshState(
                refreshing = isPullRefreshing,
                onRefresh = {
                    isPullRefreshing = true
                    notifications.refresh()
                }
            )

            val context = LocalContext.current
            val navigator = remember {
                EntryPointAccessors.fromApplication(
                    context,
                    NavigatorEntryPoint::class.java
                ).navigatorProvider()
            }

            LaunchedEffect(refreshLoadState) {
                if (refreshLoadState is LoadState.NotLoading || refreshLoadState is LoadState.Error) {
                    isPullRefreshing = false
                }
            }

            SoptTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "알림",
                                    style = SoptTheme.typography.body16M
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = onBackPressedDispatcher::onBackPressed) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left_24),
                                        contentDescription = null,
                                        tint = SoptTheme.colors.onSurface10
                                    )
                                }
                            },
                            actions = {
                                if (notifications.itemCount > 0) {
                                    Text(
                                        text = "모두 읽음",
                                        style = SoptTheme.typography.body16M,
                                        color = Orange400,
                                        modifier = Modifier
                                            .padding(end = 20.dp)
                                            .clickable{
                                                viewModel.updateEntireNotificationReadingState(
                                                    onSuccess = { notifications.refresh() }
                                                )
                                            }
                                            .padding(vertical = 8.dp, horizontal = 4.dp)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = SoptTheme.colors.background,
                                titleContentColor = SoptTheme.colors.onBackground,
                                actionIconContentColor = SoptTheme.colors.primary
                            )
                        )
                    },
                    containerColor = SoptTheme.colors.background
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .padding(start = 20.dp, top = 12.dp, bottom = 10.dp)
                        ) {
                            NotificationCategory.entries.forEach { notification ->
                                NotificationCategoryChip(
                                    category = notification.category,
                                    isSelected = state.notificationCategory == notification,
                                    onClick = {
                                        viewModel.updateNotificationCategory(category = notification)
                                    }
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(refreshState)
                        ) {
                            when (refreshLoadState) {
                                is LoadState.Loading -> {
                                    if (notifications.itemCount == 0) {
                                        LoadingIndicator()
                                    }
                                }
                                is LoadState.NotLoading -> {
                                    if (notifications.itemCount > 0) {
                                        LazyColumn {
                                            items(
                                                count = notifications.itemCount,
                                                key = { index -> notifications[index]?.notificationId ?: index }
                                            ) { index ->
                                                val notification = notifications[index]
                                                NotificationInfoItem(
                                                    notification = notification,
                                                    onCLick = {
                                                        if (notification?.notificationId == null) {
                                                            context.toast("문제가 발생했습니다.")
                                                        } else {
                                                            context.startActivity(navigator.getNotificationDetailActivityIntent(notification.notificationId))
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    } else {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                imageVector = ImageVector.vectorResource(R.drawable.icon_notification_empty),
                                                contentDescription = "알림이 없습니다."
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))
                                            Text(
                                                text = "아직 도착한 알림이 없어요.",
                                                style = SoptTheme.typography.heading18B,
                                                color = SoptTheme.colors.onSurface400
                                            )
                                        }
                                    }
                                }
                                is LoadState.Error -> {
                                    NetworkErrorDialog(
                                        onConfirm = {
                                            notifications.refresh()
                                        }
                                    )
                                }
                            }

                            PullRefreshIndicator(
                                refreshing = isPullRefreshing,
                                state = refreshState,
                                modifier = Modifier.align(Alignment.TopCenter)
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(
            context,
            NotificationActivity::class.java
        )
    }
}
