package org.sopt.official.feature.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White

@Composable
fun LoginErrorDialog(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray800)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_auth_alert_circle),
                contentDescription = "로그인 에러 아이콘",
                tint = White,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = "로그인이 안 되나요?",
                color = White,
                style = SoptTheme.typography.title20SB
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LoginDialogText(
            text = "로그인한 계정을 알고 싶어요.",
            onClick = {}
        )
        Spacer(modifier = Modifier.height(4.dp))
        LoginDialogText(
            text = "소셜 계정을 재설정하고 싶어요.",
            onClick = {}
        )
        Spacer(modifier = Modifier.height(4.dp))
        LoginDialogText(
            text = "카카오톡 채널에 문의할게요.",
            onClick = {}
        )
    }
}

@Composable
private fun LoginDialogText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if (isPressed) Gray700 else Gray800

    Text(
        text = text,
        color = White,
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick()
            }
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(10.dp),
        style = SoptTheme.typography.body16R,
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginErrorDialogPreview() {
    SoptTheme {
        LoginErrorDialog()
    }
}
