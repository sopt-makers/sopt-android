package org.sopt.official.feature.mypage.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.Black40
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.White

@Composable
fun MyPageButton(
    paddingShape: Dp,
    style: TextStyle,
    paddingVertical: Dp,
    @StringRes text: Int,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    Button(
        contentPadding = PaddingValues(paddingVertical),
        modifier = modifier.fillMaxWidth(),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black,
            disabledContainerColor = Black40,
            disabledContentColor = Gray60
        ),
        shape = RoundedCornerShape(paddingShape),
        onClick = { onButtonClick() }
    ) {
        Text(
            text = stringResource(id = text),
            style = style,
        )
    }
}
