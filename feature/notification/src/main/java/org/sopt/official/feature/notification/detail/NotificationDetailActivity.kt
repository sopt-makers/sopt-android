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
package org.sopt.official.feature.notification.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.trackViewType
import org.sopt.official.common.context.appContext
import org.sopt.official.common.navigator.NavigatorEntryPoint
import org.sopt.official.feature.notification.NotificationAnalyticsEvent
import org.sopt.official.feature.notification.NotificationAnalyticsPropertyKey
import org.sopt.official.feature.notification.toNotificationLinkType
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.components.button.MdsActionButton
import org.sopt.official.mds.components.button.MdsActionButtonSize
import org.sopt.official.mds.components.button.MdsActionButtonType
import org.sopt.official.mds.theme.SoptTheme
import org.sopt.official.model.UserStatus
import org.sopt.official.model.toViewType

private val navigator by lazy {
    EntryPointAccessors.fromApplication(
        appContext,
        NavigatorEntryPoint::class.java
    ).navigatorProvider()
}

@AndroidEntryPoint
class NotificationDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<NotificationDetailViewModel>()
    private val userStatus by lazy {
        intent.getStringExtra(USER_STATUS)
            ?.let { runCatching { UserStatus.of(it) }.getOrNull() }
            ?: UserStatus.UNAUTHENTICATED
    }

    @Inject
    lateinit var tracker: Tracker

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val notification by viewModel.notificationDetail.collectAsStateWithLifecycle()
            val context = LocalContext.current

            SoptTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = SoptTheme.colors.bg.layer.basement,
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "알림",
                                    style = SoptTheme.typography.title5
                                )
                            },
                            navigationIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(MdsIcons.chevronLeftOutlined),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .size(24.dp)
                                        .clickable(onClick = onBackPressedDispatcher::onBackPressed)
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = SoptTheme.colors.bg.layer.basement,
                                titleContentColor = SoptTheme.colors.fg.neutral.bold,
                                navigationIconContentColor = SoptTheme.colors.fg.neutral.bold
                            )
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(top = 20.dp, bottom = 16.dp)
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SoptTheme.colors.bg.neutral.ghost)
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = notification?.title.orEmpty(),
                                style = SoptTheme.typography.title4,
                                color = SoptTheme.colors.fg.neutral.bold
                            )

                            HorizontalDivider(color = SoptTheme.colors.stroke.neutral.subtle)

                            Text(
                                text = notification?.content.orEmpty(),
                                style = SoptTheme.typography.body1,
                                color = SoptTheme.colors.fg.neutral.bold
                            )
                        }
                        if (isValidLinks(deepLink = notification?.deepLink, webLink = notification?.webLink)) {
                            MdsActionButton(
                                text = "바로가기",
                                type = MdsActionButtonType.PRIMARY,
                                size = MdsActionButtonSize.LARGE,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val link = notification?.webLink
                                    ?.takeIf(String::isNotBlank)
                                    ?: notification?.deepLink

                                tracker.trackViewType(
                                    event = NotificationAnalyticsEvent.CLICK_LINK_BUTTON,
                                    viewType = userStatus.toViewType(),
                                    properties = buildMap {
                                        notification?.notificationId
                                            ?.takeIf(String::isNotBlank)
                                            ?.let { put(NotificationAnalyticsPropertyKey.NOTIFICATION_ID, it) }
                                        put(
                                            NotificationAnalyticsPropertyKey.NOTIFICATION_LINK_TYPE,
                                            link.toNotificationLinkType().value,
                                        )
                                    },
                                )

                                context.startActivity(
                                    navigator.getSchemeActivityIntent(
                                        notificationId = notification?.notificationId.orEmpty(),
                                        link = link.orEmpty()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isValidLinks(deepLink: String?, webLink: String?): Boolean =
        !deepLink.isNullOrBlank() || !webLink.isNullOrBlank()

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val notificationId = intent.getStringExtra("notificationId").orEmpty()
        viewModel.getNotificationDetail(notificationId)
    }

    companion object {
        private const val NOTIFICATION_ID = "notificationId"
        private const val USER_STATUS = "userStatus"

        @JvmStatic
        fun getIntent(
            context: Context,
            notificationId: String,
            userStatus: UserStatus,
        ) = Intent(
            context,
            NotificationDetailActivity::class.java
        ).apply {
            putExtra(NOTIFICATION_ID, notificationId)
            putExtra(USER_STATUS, userStatus.value)
        }
    }
}
