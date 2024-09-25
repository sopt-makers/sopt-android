package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray700

@Composable
internal fun TodayFortuneBox(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(
                color = Gray700,
                shape = RoundedCornerShape(12.dp),
            ),
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun TodayFortuneBoxPreview() {
    TodayFortuneBox(
        content = { Text("123") },
        modifier = Modifier.background(color = Color.White),
    )
}
