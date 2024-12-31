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
package org.sopt.official.feature.auth.feature.certificate

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.sopt.official.R.drawable.ic_auth_memeber_error
import org.sopt.official.R.drawable.ic_auth_process
import org.sopt.official.designsystem.Blue500
import org.sopt.official.designsystem.BlueAlpha100
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.Gray80
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.AuthTextField
import org.sopt.official.feature.auth.component.AuthTextWithArrow
import org.sopt.official.feature.auth.component.CertificationSnackBar
import org.sopt.official.feature.auth.component.PhoneCertification

@Composable
fun CertificationScreen() {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val onShowSnackBar: () -> Unit = remember {
        {
            coroutineScope.launch {
                snackBarHostState.showSnackbar("인증번호가 전송되었어요.")
            }
        }
    }

    SnackbarHost(
        hostState = snackBarHostState,
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        snackbar = { message ->
            CertificationSnackBar(message = message.visuals.message)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(72.dp))
        TopBar()
        Spacer(modifier = Modifier.height(44.dp))
        PhoneCertification(
            onPhoneNumberClick = onShowSnackBar,
            textColor = Gray80
        )
        Spacer(modifier = Modifier.height(10.dp))
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            labelText = "",
            hintText = "인증번호를 입력해 주세요.",
            onTextChange = {},
        )
        Spacer(modifier = Modifier.height(41.dp))
        AuthButton(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    color = Blue500,
                    width = 1.dp,
                    shape = RoundedCornerShape(10.dp)
                ),
            padding = PaddingValues(vertical = 14.dp, horizontal = 18.dp),
            onClick = {},
            containerColor = BlueAlpha100,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(
                    painterResource(id = ic_auth_memeber_error),
                    contentDescription = "에러 아이콘",
                )
                Column {
                    AuthTextWithArrow(
                        text = "SOPT 회원 인증에 실패하셨나요?",
                        textStyle = SoptTheme.typography.body14M
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "번호가 바뀌었거나, 인증이 어려우신 경우 추가 정보 인증을 통해 가입을 도와드리고 있어요!",
                        color = Gray60
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AuthButton(
            modifier = Modifier.fillMaxWidth(),
            padding = PaddingValues(vertical = 16.dp),
            onClick = {},
            containerColor = Gray10,
            contentColor = Gray950,
            disabledContentColor = Gray60,
        ) {
            Text(
                text = "SOPT 회원 인증 완료",
                style = SoptTheme.typography.body14M
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = ic_auth_process),
            contentDescription = "상단 이미지"
        )
        Spacer(modifier = Modifier.height(11.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(66.dp)) {
            Text(
                text = "SOPT 회원인증",
                color = White,
                style = SoptTheme.typography.label12SB
            )
            Text(
                text = "소셜 계정 연동",
                color = Gray100,
                style = SoptTheme.typography.label12SB
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "SOPT 회원인증",
            color = Gray10,
            style = SoptTheme.typography.heading24B
        )
        Text(
            text = "Playground는 SOPT 회원만을 위한 공간이에요.\nSOPT 회원인증을 위해 전화번호를 입력해 주세요.",
            color = Gray60,
            style = SoptTheme.typography.label12SB
        )
    }
}

internal enum class ErrorCase(val message: String) {
    CODE_ERROR("인증번호가 일치하지 않아요.\n번호를 확인한 후 다시 입력해 주세요."),
    PHONE_ERROR("솝트 활동 시 사용한 전화번호가 아니예요.\n인증을 실패하신 경우 하단에서 다른 방법으로 인증할 수 있어요."),
    TIME_ERROR("3분이 초과되었어요. 인증번호를 다시 요청해주세요.")
}

@Preview(showBackground = true)
@Composable
private fun AuthCertificationPreview() {
    SoptTheme {
        CertificationScreen()
    }
}
