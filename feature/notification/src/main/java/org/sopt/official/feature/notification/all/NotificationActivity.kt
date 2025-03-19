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
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import java.util.Date
import java.util.Locale
import org.sopt.official.common.navigator.NavigatorEntryPoint
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.notification.R

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private val viewModel by viewModels<NotificationViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val notifications = viewModel.notifications.collectAsLazyPagingItems()
            val context = LocalContext.current
            val navigator = remember {
                EntryPointAccessors.fromApplication(
                    context,
                    NavigatorEntryPoint::class.java
                ).navigatorProvider()
            }
            SoptTheme {
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
                                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null,
                                        tint = SoptTheme.colors.onBackground
                                    )
                                }
                            },
                            actions = {
                                Text(
                                    text = "모두 읽음",
                                    style = SoptTheme.typography.body16M,
                                    color = SoptTheme.colors.primary,
                                    modifier = Modifier
                                        .padding(end = 20.dp)
                                        .clickable(onClick = viewModel::updateEntireNotificationReadingState)
                                        .padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = SoptTheme.colors.background,
                                titleContentColor = SoptTheme.colors.onBackground,
                                actionIconContentColor = SoptTheme.colors.primary
                            )
                        )
                    }) { innerPadding ->
                    if (notifications.itemCount > 0) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(SoptTheme.colors.background)
                        ) {
                            items(notifications.itemCount) {
                                val item = notifications[it]
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clickable {
                                            context.startActivity(navigator.getNotificationDetailActivityIntent(item?.notificationId.orEmpty()))
                                        }
                                        .background(
                                            if (item?.isRead == true) {
                                                SoptTheme.colors.onSurface800
                                            } else {
                                                SoptTheme.colors.background
                                            }
                                        )
                                        .padding(
                                            horizontal = 20.dp,
                                            vertical = 16.dp
                                        )) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            item?.title.orEmpty(),
                                            style = SoptTheme.typography.body16M,
                                            color = SoptTheme.colors.onSurface30,
                                            modifier = Modifier
                                                .weight(1f)
                                                .widthIn(max = 250.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            item?.createdAt.orEmpty().convertToTimesAgo(),
                                            style = SoptTheme.typography.body13M.copy(fontSize = 12.sp),
                                            color = SoptTheme.colors.onSurface100
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        item?.content.orEmpty(),
                                        style = SoptTheme.typography.body16M,
                                        color = SoptTheme.colors.onSurface400,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                HorizontalDivider(color = SoptTheme.colors.onSurface600)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(SoptTheme.colors.background),
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
                                color = SoptTheme.colors.onSurface800
                            )
                        }
                    }
                }
            }
        }
    }

    private fun String.convertToTimesAgo(): String {
        val dateFormat: DateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
            Locale.KOREA
        )
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        val currentDate = Date()
        val receivedDate = dateFormat.parse(this)
        val diffInMillis = currentDate.time - receivedDate.time

        val diffInDays = diffInMillis / ONE_DAY_IN_MILLISECONDS
        val diffInHours = diffInMillis / ONE_HOUR_IN_MILLISECONDS
        val diffInMinutes = diffInMillis / ONE_MINUTE_IN_MILLISECONDS

        return when {
            diffInDays >= 1 -> "${diffInDays}일 전"
            diffInHours >= 1 -> "${diffInHours}시간 전"
            diffInMinutes >= 1 -> "${diffInMinutes}분 전"
            else -> "방금"
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(
            context,
            NotificationActivity::class.java
        )

        const val ONE_DAY_IN_MILLISECONDS = 86400000L
        const val ONE_HOUR_IN_MILLISECONDS = 3600000L
        const val ONE_MINUTE_IN_MILLISECONDS = 60000L
    }
}
