package org.sopt.official.feature.auth.feature.authcertification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Black60
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun AuthTextField(
    text: String,
    hintText: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier
            .background(color = Black60, shape = RoundedCornerShape(10.dp))
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .padding(vertical = 15.dp, horizontal = 20.dp),
        textStyle = SoptTheme.typography.body14M.copy(color = Gray10),
        decorationBox = { innerTextField ->
            if (text.isEmpty())
                Text(
                    text = hintText,
                    color = Gray100,
                    style = SoptTheme.typography.body14M
                )
            innerTextField()
        }
    )
}
