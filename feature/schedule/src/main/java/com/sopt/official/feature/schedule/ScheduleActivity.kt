package com.sopt.official.feature.schedule

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.official.feature.schedule.component.ScheduleItem
import com.sopt.official.feature.schedule.component.VerticalDividerWithCircle
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.designsystem.SoptTheme

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScheduleScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    val fakeData = listOf(
        Pair("9월 28일 토요일", "1차 세미나"),
        Pair("9월 28일 토요일", "2차 세미나"),
        Pair("9월 28일 토요일", "3차 세미나"),
        Pair("9월 28일 토요일", "4차 세미나"),
        Pair("9월 28일 토요일", "5차 세미나"),
        Pair("9월 28일 토요일", "6차 세미나"),
        Pair("9월 28일 토요일", "7차 세미나"),
        Pair("9월 28일 토요일", "8차 세미나"),
        Pair("9월 28일 토요일", "9차 세미나"),
        Pair("9월 28일 토요일", "10차 세미나"),
    )

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
                items(fakeData) { item ->
                    ScheduleItem(
                        date = item.first,
                        event = item.second
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
fun ScheduleActivityPreview() {
    SoptTheme {
        ScheduleScreen()
    }
}
