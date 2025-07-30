package org.sopt.official.designsystem.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.designsystem.SoptTheme

@Composable
fun OneButtonDialog(
    onDismiss: () -> Unit,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
    content: @Composable () -> Unit,
) {
    SoptBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier,
        properties = properties
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            content()

            Button(
                onClick = onButtonClick,
                contentPadding = PaddingValues(vertical = 11.dp),
                colors = ButtonColors(
                    containerColor = SoptTheme.colors.primary,
                    contentColor = SoptTheme.colors.onSurface,
                    disabledContainerColor = SoptTheme.colors.primary,
                    disabledContentColor = SoptTheme.colors.onSurface
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buttonText,
                    style = SoptTheme.typography.label16SB,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 812)
@Composable
private fun OneButtonDialogPreview() {
    SoptTheme {
        OneButtonDialog(
            onDismiss = {},
            buttonText = "확인",
            onButtonClick = {}
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "다이얼로그 테스트",
                    style = SoptTheme.typography.title18SB,
                    color = SoptTheme.colors.primary
                )
                Text(
                    text = "버튼이 하나인 다이얼로그지롱",
                    style = SoptTheme.typography.body14R,
                    color = SoptTheme.colors.onSurface100
                )
            }
        }
    }
}
