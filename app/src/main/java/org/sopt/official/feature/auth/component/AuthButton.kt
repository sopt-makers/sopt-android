package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.Black80
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White

@Composable
internal fun AuthButton(
    padding: PaddingValues,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    isEnabled: Boolean = true,
    containerColor: Color = White,
    contentColor: Color = Black,
    disabledContainerColor: Color = Black80,
    disabledContentColor: Color = Gray60,
    content: @Composable () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        contentPadding = padding,
        shape = shape,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        )
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthButtonPreview() {
    SoptTheme {
        AuthButton(
            padding = PaddingValues(16.dp),
            onClick = {},
        ) {
            Text("로그인")
        }
    }
}