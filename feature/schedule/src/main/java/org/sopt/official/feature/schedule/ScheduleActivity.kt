package org.sopt.official.feature.schedule

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.schedule.component.ScheduleItem
import org.sopt.official.feature.schedule.component.VerticalDividerWithCircle

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoptTheme {
                ScheduleScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
) {
    val state by viewModel.schedule.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SoptTheme.colors.background,
                    titleContentColor = SoptTheme.colors.surface
                ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "뒤로 가기",
                        tint = SoptTheme.colors.surface,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                title = {
                    Text(
                        text = "일정",
                        style = SoptTheme.typography.body16M,
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(SoptTheme.colors.background)
                .padding(bottom = 34.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(state.scheduleList) { item ->
                    ScheduleItem(
                        date = item.date,
                        event = item.title
                    )
                }

                item {
                    VerticalDividerWithCircle(Color.Unspecified)
                }
            }

            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color(0x000F1010),
                                    Color(0xFF0F1010),
                                ),
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SoptTheme.colors.primary)
                        .clickable {
                            // TODO: nav 연결
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "출석하러 가기",
                        style = SoptTheme.typography.label18SB,
                        color = SoptTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScheduleActivityPreview() {
    SoptTheme {
        ScheduleScreen()
    }
}
