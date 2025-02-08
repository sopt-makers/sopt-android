package org.sopt.official.feature.schedule.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun ScheduleItem(
    date: String,
    event: String,
) {
    Row {
        VerticalDividerWithCircle()
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = date,
                color = SoptTheme.colors.primary,
                style = SoptTheme.typography.body14M,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "세미나",
                    color = SoptTheme.colors.primary,
                    modifier = Modifier
                        .background(SoptTheme.colors.error, RoundedCornerShape(4.dp)) // 임시 색상 입니다
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    style = SoptTheme.typography.label11SB,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = event,
                    color = SoptTheme.colors.onSurface10, // 임시 색상 입니다
                    style = SoptTheme.typography.heading18B
                )
            }
        }

    }
}

@Preview
@Composable
private fun ScheduleItemPreview() {
    SoptTheme {
        ScheduleItem(
            date = "9월 28일 토요일",
            event = "1차 세미나"
        )
    }
}
