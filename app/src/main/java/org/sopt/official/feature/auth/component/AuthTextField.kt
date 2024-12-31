/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R.drawable.ic_auth_certification_error
import org.sopt.official.designsystem.Black60
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.utils.phoneNumberVisualTransformation

@Composable
internal fun AuthTextField(
    labelText: String,
    hintText: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    suffix: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .then(
                    if (!isError) {
                        Modifier
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = SoptTheme.colors.error,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                )
                .background(
                    color = Black60,
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
                        color = Gray100,
                        style = SoptTheme.typography.body16M
                    )
                }

                BasicTextField(
                    value = labelText,
                    onValueChange = onTextChange,
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    textStyle = SoptTheme.typography.body16M.copy(color = Gray10),
                )
            }
            // suffix가 있으면 우측에 표시
            suffix?.let {
                Spacer(modifier = Modifier.width(8.dp))
                it()
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
