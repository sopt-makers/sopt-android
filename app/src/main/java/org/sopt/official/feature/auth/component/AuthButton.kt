package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.Black80
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.White

@Composable
fun AuthButton(
    paddingVertical: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    paddingHorizontal: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(10.dp),
    containerColor: Color = White,
    contentColor: Color = Black,
    isEnabled: Boolean = true,
    disabledContainerColor: Color = Black80,
    disabledContentColor: Color = Gray60,
    content: @Composable () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        contentPadding = PaddingValues(vertical = paddingVertical, horizontal = paddingHorizontal),
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
