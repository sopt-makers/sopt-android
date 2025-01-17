/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray80
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.auth.feature.certificate.CertificationButtonText

@Composable
internal fun PhoneCertification(
    onPhoneNumberClick: () -> Unit,
    textColor: Color,
    onTextChange: (String) -> Unit,
    phoneNumber: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    buttonText: String
) {
    Column(modifier = modifier) {
        Text(
            text = "전화번호",
            color = textColor,
            style = SoptTheme.typography.body14M
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                labelText = phoneNumber,
                hintText = "010-XXXX-XXXX",
                onTextChange = onTextChange,
                visualTransformation = visualTransformation
            )
            AuthButton(
                padding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = if (buttonText == CertificationButtonText.GET_CODE.message) 12.dp
                    else 20.dp
                ),
                onClick = onPhoneNumberClick,
                containerColor = Gray10,
                contentColor = Gray950,
            ) {
                Text(
                    text = buttonText,
                    style = SoptTheme.typography.body14M
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneCertificationPreview() {
    SoptTheme {
        PhoneCertification(
            onPhoneNumberClick = {},
            textColor = Gray80,
            onTextChange = {},
            phoneNumber = "01012345678",
            buttonText = "인증번호 요청"
        )
    }
}
