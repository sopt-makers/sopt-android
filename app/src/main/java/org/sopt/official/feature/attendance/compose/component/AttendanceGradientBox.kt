package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AttendanceGradientBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(148.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x000F1010), Color(0xFF0F1010)
                    )
                )
            )
            .padding(bottom = 41.dp)
    )
}

@Preview
@Composable
private fun AttendanceGradientBoxPreview() {
    AttendanceGradientBox()
}