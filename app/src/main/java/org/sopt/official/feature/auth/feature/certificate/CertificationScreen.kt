/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.R.drawable.ic_auth_memeber_error
import org.sopt.official.R.drawable.ic_auth_process_first
import org.sopt.official.common.view.toast
import org.sopt.official.designsystem.Blue500
import org.sopt.official.designsystem.BlueAlpha100
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray500
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.Gray80
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.Gray950
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.AuthNavigationText
import org.sopt.official.feature.auth.component.AuthTextField
import org.sopt.official.feature.auth.feature.certificate.navigation.SocialAccountInfo
import org.sopt.official.feature.auth.model.AuthStatus
import org.sopt.official.feature.auth.utils.phoneNumberVisualTransformation

@Composable
internal fun CertificationRoute(
    status: AuthStatus,
    onBackClick: () -> Unit,
    onShowSnackBar: () -> Unit,
    navigateToSocialAccount: (SocialAccountInfo) -> Unit,
    navigateToAuthMain: (String) -> Unit,
    onGoogleFormClick: () -> Unit,
    viewModel: CertificationViewModel = metroViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is CertificationSideEffect.ShowToast -> context.toast(sideEffect.message)

                    is CertificationSideEffect.ShowSnackBar -> onShowSnackBar()

                    is CertificationSideEffect.NavigateToSocialAccount -> {
                        viewModel.timerJob?.cancelAndJoin()
                        viewModel.timerJob = null
                        navigateToSocialAccount(
                            SocialAccountInfo(
                                status = status,
                                name = sideEffect.name,
                                phone = sideEffect.phone
                            )
                        )
                    }

                    is CertificationSideEffect.NavigateToAuthMain -> {
                        viewModel.timerJob?.cancelAndJoin()
                        viewModel.timerJob = null
                        navigateToAuthMain(sideEffect.platform)
                    }
                }
            }
    }

    CertificationScreen(
        status = status,
        currentTime = state.currentTime,
        onBackClick = onBackClick,
        onCreateCodeClick = {
            viewModel.resetErrorCase()
            scope.launch {
                viewModel.createCode(status)
            }
        },
        onCertificateClick = {
            viewModel.certificateCode(status)
        },
        onGoogleFormClick = onGoogleFormClick,
        onPhoneNumberChange = { newPhoneNumber ->
            viewModel.updatePhone(newPhoneNumber)
            viewModel.resetErrorCase()
        },
        onCodeChange = { newCode ->
            viewModel.updateCode(newCode)
            viewModel.resetErrorCase()
        },
        phoneNumber = state.phone,
        code = state.code,
        visualTransformation = phoneNumberVisualTransformation(),
        errorMessage = state.errorMessage,
        certificationButtonText = state.buttonText,
        isCodeEnable = state.isCodeEnable,
        isCertificationButtonEnable = state.isCertificationButtonEnable,
        isFinishButtonEnable = state.isFinishButtonEnable
    )
}

@Composable
private fun CertificationScreen(
    status: AuthStatus,
    currentTime: String,
    onBackClick: () -> Unit,
    onCreateCodeClick: () -> Unit,
    onCertificateClick: () -> Unit,
    onGoogleFormClick: () -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    phoneNumber: String,
    code: String,
    visualTransformation: VisualTransformation,
    errorMessage: ErrorCase,
    certificationButtonText: String,
    isCodeEnable: Boolean,
    isCertificationButtonEnable: Boolean,
    isFinishButtonEnable: Boolean
) {
    Column {
        Image(
            painter = painterResource(id = R.drawable.ic_auth_arrow_left),
            contentDescription = "뒤로가기 버튼",
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = 20.dp)
                .clickable { onBackClick() }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(status = status)
            Spacer(modifier = Modifier.height(44.dp))
            PhoneCertification(
                onPhoneNumberClick = onCreateCodeClick,
                textColor = Gray80,
                onTextChange = onPhoneNumberChange,
                phoneNumber = phoneNumber,
                visualTransformation = visualTransformation,
                buttonText = certificationButtonText,
                errorMessage = errorMessage,
                isCertificationButtonEnable = isCertificationButtonEnable
            )
            Spacer(modifier = Modifier.height(10.dp))
            AuthTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = code,
                hintText = "인증번호를 입력해 주세요.",
                onTextChange = onCodeChange,
                isError = ErrorCase.isCodeError(errorMessage),
                isEnabled = isCodeEnable,
                errorMessage = errorMessage.message
            ) {
                Text(
                    text = currentTime,
                    style = SoptTheme.typography.body14M,
                    color = White,
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            if (status == AuthStatus.REGISTER) {
                AuthButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            color = Blue500,
                            width = 1.dp,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    padding = PaddingValues(vertical = 14.dp, horizontal = 18.dp),
                    onClick = onGoogleFormClick,
                    containerColor = BlueAlpha100,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Image(
                            painterResource(id = ic_auth_memeber_error),
                            contentDescription = "에러 아이콘",
                        )
                        Column {
                            AuthNavigationText(
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
                Spacer(modifier = Modifier.height(20.dp))
            }

            AuthButton(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(vertical = 16.dp),
                onClick = onCertificateClick,
                isEnabled = isFinishButtonEnable,
                containerColor = Gray10,
                contentColor = Gray950,
                disabledContentColor = Gray500,
                disabledContainerColor = Gray800
            ) {
                Text(
                    text = "SOPT 회원 인증 완료",
                    style = SoptTheme.typography.body14M
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TopBar(
    status: AuthStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = ic_auth_process_first),
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
                text = if (status == AuthStatus.REGISTER) "소셜 계정 연동" else "소셜 계정 재설정",
                color = Gray100,
                style = SoptTheme.typography.label12SB
            )
        }
        Spacer(modifier = Modifier.height(54.dp))
        Text(
            text = "SOPT 회원인증",
            color = Gray10,
            style = SoptTheme.typography.heading24B
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            textAlign = TextAlign.Center,
            text = "이곳은 SOPT 회원만을 위한 공간이에요.\nSOPT 회원인증을 위해 전화번호를 입력해 주세요.",
            color = Gray60,
            style = SoptTheme.typography.label12SB
        )
    }
}

@Composable
private fun PhoneCertification(
    onPhoneNumberClick: () -> Unit,
    textColor: Color,
    onTextChange: (String) -> Unit,
    phoneNumber: String,
    buttonText: String,
    errorMessage: ErrorCase,
    isCertificationButtonEnable: Boolean,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier) {
        Text(
            text = "전화번호",
            color = textColor,
            style = SoptTheme.typography.body14M
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            AuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                labelText = phoneNumber,
                hintText = "010-XXXX-XXXX",
                onTextChange = onTextChange,
                visualTransformation = visualTransformation,
                isError = ErrorCase.isPhoneError(errorMessage),
                errorMessage = errorMessage.message
            )
            AuthButton(
                padding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = if (buttonText == CertificationButtonText.GET_CODE.message) 24.dp
                    else 20.dp
                ),
                onClick = onPhoneNumberClick,
                containerColor = Gray10,
                contentColor = Gray950,
                isEnabled = isCertificationButtonEnable
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
private fun AuthCertificationPreview() {
    SoptTheme {
        CertificationScreen(
            status = AuthStatus.REGISTER,
            currentTime = "03:00",
            onBackClick = {},
            onCreateCodeClick = {},
            onCertificateClick = {},
            onGoogleFormClick = {},
            onPhoneNumberChange = {},
            onCodeChange = {},
            phoneNumber = "01012345678",
            code = "132456",
            visualTransformation = phoneNumberVisualTransformation(),
            errorMessage = ErrorCase.NONE,
            certificationButtonText = CertificationButtonText.GET_CODE.message,
            isCodeEnable = true,
            isCertificationButtonEnable = true,
            isFinishButtonEnable = true
        )
    }
}
