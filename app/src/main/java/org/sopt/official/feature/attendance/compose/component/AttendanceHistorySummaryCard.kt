package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AttendanceHistorySummaryCard(modifier: Modifier = Modifier) {
    Row(modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "전체",
                color = SoptTheme.colors.onSurface300,
                style = SoptTheme.typography.label12SB
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "asdf",
                color = SoptTheme.colors.onSurface50,
                style = SoptTheme.typography.body14M
            )
        }
    }
}

@Preview
@Composable
fun AttendanceHistorySummaryCardPreview() {
    SoptTheme {
        AttendanceHistorySummaryCard()
    }
}