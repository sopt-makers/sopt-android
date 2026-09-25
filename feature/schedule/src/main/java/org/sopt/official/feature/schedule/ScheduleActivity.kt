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
package org.sopt.official.feature.schedule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.deeplinkdispatch.DeepLink
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import org.sopt.official.common.context.appContext
import org.sopt.official.common.navigator.NavigatorEntryPoint
import org.sopt.official.feature.schedule.component.ScheduleItem
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.components.button.MdsActionButton
import org.sopt.official.mds.components.button.MdsActionButtonSize
import org.sopt.official.mds.components.button.MdsActionButtonType
import org.sopt.official.mds.theme.SoptTheme
import org.sopt.official.model.UserStatus

private val applicationNavigator by lazy {
    EntryPointAccessors.fromApplication(
        appContext,
        NavigatorEntryPoint::class.java
    ).navigatorProvider()
}

@AndroidEntryPoint
@DeepLink("sopt://schedule")
class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoptTheme {
                ScheduleScreen(
                    navigateUp = ::finish,
                    navigateAttendance = {
                        startActivity(applicationNavigator.getAttendanceActivityIntent())
                    }
                )
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ScheduleActivity::class.java)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleScreen(
    navigateUp: () -> Unit = {},
    navigateAttendance: () -> Unit = {},
    viewModel: ScheduleViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()
    val state by viewModel.schedule.collectAsStateWithLifecycle()
    val userStatus by viewModel.userStatus.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        if (state.scheduleList.isNotEmpty()) {
            val itemIndex = state.scheduleList.indexOfFirst { it.isRecentSchedule }
            delay(200L)
            lazyListState.animateScrollToItem(if (itemIndex <= 0) 0 else itemIndex)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SoptTheme.colors.bg.layer.basement,
                    titleContentColor = SoptTheme.colors.fg.neutral.bold
                ),
                navigationIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(MdsIcons.chevronLeftOutlined),
                        contentDescription = "뒤로 가기",
                        tint = SoptTheme.colors.fg.neutral.bold,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp)
                            .clickable(onClick = navigateUp)
                            .padding(8.dp)
                    )
                },
                title = {
                    Text(
                        text = "일정",
                        style = SoptTheme.typography.title4,
                    )
                }
            )
        },
        containerColor = SoptTheme.colors.bg.layer.basement
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            VerticalDivider(
                thickness = Dp.Hairline,
                color = SoptTheme.colors.stroke.neutral.default,
                modifier = Modifier
                    .padding(start = 28.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(bottom = 200.dp),
                state = lazyListState
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .background(SoptTheme.colors.bg.layer.basement),
                    )
                }

                items(state.scheduleList) { item ->
                    ScheduleItem(
                        date = item.date,
                        title = item.title,
                        type = item.type,
                        isRecentSchedule = item.isRecentSchedule
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x000F1010),
                                Color(0xFF0F1010)
                            )
                        )
                    )
            )

            if (userStatus == UserStatus.ACTIVE) {
                MdsActionButton(
                    text = "출석하러 가기",
                    type = MdsActionButtonType.PRIMARY,
                    size = MdsActionButtonSize.LARGE,
                    onClick = navigateAttendance,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                )
            }
        }
    }
}
