package org.sopt.official.feature.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R.drawable.ic_auth_certification_error
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray200
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray500
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.BasicTextDefaults.MAX_CODE_NUMBER
import org.sopt.official.feature.auth.component.BasicTextDefaults.MAX_PHONE_NUMBER
import org.sopt.official.feature.auth.component.BasicTextDefaults.PHONE_HINT_TEXT
import org.sopt.official.feature.auth.utils.phoneNumberVisualTransformation

@Composable
internal fun AuthTextField(
    labelText: String,
    hintText: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    errorMessage: String? = null,
    suffix: (@Composable () -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .then(
                    Modifier.border(
                        width = 1.dp,
                        color = when {
                            isError -> SoptTheme.colors.error
                            isFocused -> Gray200
                            else -> Gray800
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
                )
                .background(
                    color = Gray800,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(vertical = 15.dp, horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                // Hint(Placeholder) 처리
                if (labelText.isEmpty()) {
                    Text(
                        text = hintText,
                        color = if (isEnabled) Gray300 else Gray500,
                        style = SoptTheme.typography.body16M
                    )
                }

                BasicTextField(
                    value = labelText,
                    onValueChange = { newValue ->
                        val filteredValue = newValue.filter { it.isDigit() }
                        val maxLength = if (hintText.contains(PHONE_HINT_TEXT)) MAX_PHONE_NUMBER else MAX_CODE_NUMBER
                        if (filteredValue.length <= maxLength) {
                            onTextChange(filteredValue)
                        }
                    },
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    textStyle = SoptTheme.typography.body16M.copy(color = Gray10),
                    modifier = Modifier.onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                    enabled = isEnabled
                )
            }

            if (isEnabled) {
                // suffix가 있으면 우측에 표시
                suffix?.let {
                    Spacer(modifier = Modifier.width(8.dp))
                    it()
                }
            }
        }
        if (isError && !errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(id = ic_auth_certification_error),
                    contentDescription = "에러 아이콘",
                )
                Text(
                    text = errorMessage,
                    color = SoptTheme.colors.error,
                    style = SoptTheme.typography.label12SB,
                )
            }
        }
    }
}

object BasicTextDefaults {
    const val PHONE_HINT_TEXT: String = "010"
    const val MAX_PHONE_NUMBER: Int = 11
    const val MAX_CODE_NUMBER: Int = 6
}

@Preview(showBackground = false)
@Composable
private fun AuthTextFieldPreview() {
    SoptTheme {
        var text by remember { mutableStateOf("") }
        AuthTextField(
            labelText = text,
            hintText = "이메일",
            onTextChange = { text = it },
            isError = text == "에러",
            suffix = {
                Text(
                    text = "이메일",
                    color = White
                )
            },
            visualTransformation = phoneNumberVisualTransformation(),
            errorMessage = "이메일 형식이 아닙니다."
        )
    }
}