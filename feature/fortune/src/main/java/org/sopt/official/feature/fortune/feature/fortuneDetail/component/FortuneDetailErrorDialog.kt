package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography

@Composable
internal fun FortuneDetailErrorDialog(
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onCheckClick) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = colors.onBackground,
                    shape = RoundedCornerShape(size = 10.dp),
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                Spacer(modifier = Modifier.height(height = 26.dp))
                Text(
                    text = "네트워크가 원활하지 않습니다.",
                    style = typography.heading16B,
                    color = colors.onSurface800,
                )
                Spacer(modifier = Modifier.height(height = 24.dp))
                Text(
                    text = "인터넷 연결을 확인하고 다시 시도해 주세요.",
                    style = typography.body14M,
                    color = colors.onSurface100,
                )
                Spacer(modifier = Modifier.height(height = 34.dp))
                Button(
                    onClick = onCheckClick,
                    shape = RoundedCornerShape(size = 10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 128.dp,
                        vertical = 12.dp,
                    ),
                    colors = ButtonDefaults.buttonColors(containerColor = Black),
                ) {
                    Text(
                        text = "확인",
                        style = typography.body14R,
                        color = colors.onSurface10,
                    )
                }
                Spacer(modifier = Modifier.height(height = 10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FortuneDetailErrorDialogPreview() {
    SoptTheme {
        FortuneDetailErrorDialog(
            onCheckClick = { },
        )
    }
}
