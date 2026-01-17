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
package org.sopt.official.feature.notification.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.common.navigator.SOPTLOG_FORTUNE
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.di.SoptViewModelFactory
import org.sopt.official.feature.notification.R
import org.sopt.official.feature.notification.detail.component.ErrorSnackBar
import java.time.LocalDate

@Inject
class NotificationDetailActivity(
    private val viewModelFactory: SoptViewModelFactory,
    private val navigatorProvider: NavigatorProvider
) : AppCompatActivity() {
    
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    private val viewModel by viewModels<NotificationDetailViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val notification by viewModel.notificationDetail.collectAsStateWithLifecycle()
            val context = LocalContext.current

            val snackBarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            val onShowErrorSnackBar: (String?) -> Unit = { text ->
                coroutineScope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()

                    snackBarHostState.showSnackbar(
                        message = text ?: "오류가 발생했어요. 다시 시도해주세요.",
                    )
                }
            }

            SoptTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SoptTheme.colors.background),
                    containerColor = SoptTheme.colors.background,
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
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = SoptTheme.colors.background,
                                titleContentColor = SoptTheme.colors.onBackground,
                                navigationIconContentColor = SoptTheme.colors.onBackground
                            )
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(SoptTheme.colors.background)
                            .padding(top = 20.dp)
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SoptTheme.colors.onSurface800)
                                .padding(
                                    vertical = 24.dp,
                                    horizontal = 12.dp
                                )
                        ) {
                            Text(
                                text = notification?.title.orEmpty(),
                                style = SoptTheme.typography.heading18B,
                                color = SoptTheme.colors.onSurface10
                            )
                            Spacer(modifier = Modifier.padding(14.dp))
                            HorizontalDivider(color = SoptTheme.colors.onSurface400)
                            Text(
                                text = notification?.content.orEmpty(),
                                style = SoptTheme.typography.body16M,
                                color = SoptTheme.colors.onSurface10,
                                modifier = Modifier.padding(top = 24.dp)
                            )
                        }
                        if (isValidLinks(deepLink = notification?.deepLink, webLink = notification?.webLink)) {
                            Column {
                                Button(
                                    onClick = {
                                        val link = notification?.webLink ?: notification?.deepLink

                                        when {
                                            link == SOPTLOG_FORTUNE && !isToday(notification?.createdAt?.split("T")?.get(0)) -> {
                                                onShowErrorSnackBar("앗, 오늘의 솝마디만 볼 수 있어요.")
                                            }

                                            else -> {
                                                context.startActivity(
                                                    navigatorProvider.getSchemeActivityIntent(
                                                        notificationId = notification?.notificationId.orEmpty(),
                                                        link = link.orEmpty()
                                                    )
                                                )
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SoptTheme.colors.primary,
                                        contentColor = SoptTheme.colors.onPrimary
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "바로가기 >",
                                        style = SoptTheme.typography.body16M
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                            }
                        }
                    }
                }

                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    ErrorSnackBar(message = it.visuals.message)
                }
            }
        }
    }

    private fun isValidLinks(deepLink: String?, webLink: String?): Boolean =
        !deepLink.isNullOrBlank() || !webLink.isNullOrBlank()

    private fun isToday(date: String?): Boolean = LocalDate.now().toString() == date

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val notificationId = intent.getStringExtra("notificationId").orEmpty()
        viewModel.getNotificationDetail(notificationId)
    }

    companion object {
        @JvmStatic
        fun getIntent(
            context: Context,
            notificationId: String,
        ) = Intent(
            context,
            NotificationDetailActivity::class.java
        ).putExtra(
            "notificationId",
            notificationId
        )
    }
}