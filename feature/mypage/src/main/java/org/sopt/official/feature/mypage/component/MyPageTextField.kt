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
import org.sopt.official.designsystem.Black80
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.mypage.R

@Composable
fun MyPageTextField(
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        modifier = modifier
            .fillMaxWidth()
            .background(color = Black80, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (isFocused) White else Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .padding(horizontal = 20.dp)
            .padding(vertical = 16.dp),
        textStyle = SoptTheme.typography.body18M.copy(color = White),
        decorationBox = { innerTextField ->
            if (text.isEmpty())
                Text(
                    text = stringResource(id = R.string.adjust_sentence_hint),
                    color = Gray60,
                    style = SoptTheme.typography.body16M
                )
            innerTextField()
        }
    )
}