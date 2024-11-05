package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.R
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White

@Composable
fun AuthTextWithButton(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = SoptTheme.typography.label14SB,
    textColor: Color = White,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle
        )
        Icon(
            painter = painterResource(R.drawable.ic_auth_arrow_right),
            contentDescription = "화살표 버튼",
            tint = White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthTextWithButtonPreview() {
    SoptTheme {
        AuthTextWithButton(
            text = "text",
            textColor = White,
            textStyle = SoptTheme.typography.label12SB
        )
    }
}