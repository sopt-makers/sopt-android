package org.sopt.official.feature.auth.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.White

@Composable
fun AuthButton(
    paddingVertical: Dp,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier.fillMaxWidth(),
    containerColor: Color = White,
    contentColor: Color = Black,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(vertical = paddingVertical),
        shape = shape,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        content()
    }
}
