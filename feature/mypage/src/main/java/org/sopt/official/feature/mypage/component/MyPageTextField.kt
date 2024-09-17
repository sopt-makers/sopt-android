package org.sopt.official.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.mypage.R

@Composable
fun MyPageTextField(
    sentence: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = sentence,
        onValueChange = onTextChange,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Gray800, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (isFocused) White else Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .padding(
                top = 16.dp,
                bottom = 36.dp,
                start = 20.dp
            ),
        textStyle = SoptTheme.typography.body16M.copy(color = White),
        decorationBox = { innerTextField ->
            if (sentence.isEmpty())
                Text(
                    text = stringResource(id = R.string.adjust_sentence_hint),
                    color = Gray300,
                    style = SoptTheme.typography.body16M
                )
            innerTextField()
        }
    )
}