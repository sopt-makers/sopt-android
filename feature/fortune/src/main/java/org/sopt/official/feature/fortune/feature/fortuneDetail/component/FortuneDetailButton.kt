package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White

@Composable
internal fun FortuneDetailButton(
    onButtonClick: () -> Unit,
    buttonTitle: String,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onButtonClick,
        shape = RoundedCornerShape(size = 12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = White),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = buttonTitle,
            style = SoptTheme.typography.label18SB,
            color = Black,
            modifier = Modifier.padding(
                horizontal = 94.dp,
                vertical = 16.dp,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FortuneDetailButtonPreview() {
    SoptTheme {
        FortuneDetailButton(
            onButtonClick = { },
            buttonTitle = "오늘 부적",
        )
    }
}
