package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.SoptTheme

@Composable
fun TodayNoScheduleCard(modifier: Modifier = Modifier) {
    Column(
        modifier =
        modifier
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        Text(
            text = stringResource(id = R.string.text_no_schedule),
            color = SoptTheme.colors.onSurface10,
            style = SoptTheme.typography.title16SB,
        )
    }
}

@Preview
@Composable
private fun TodayNoScheduleCardPreview() {
    SoptTheme {
        Column(modifier = Modifier.background(color = SoptTheme.colors.background)) {
            TodayNoScheduleCard()
        }
    }
}