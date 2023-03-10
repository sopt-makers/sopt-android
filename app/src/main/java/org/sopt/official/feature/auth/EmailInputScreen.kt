package org.sopt.official.feature.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.sopt.official.config.navigation.AuthNavGraph
import org.sopt.official.designsystem.style.SoptTheme
import org.sopt.official.designsystem.style.White
import org.sopt.official.domain.repository.AuthRepository
import org.sopt.official.domain.usecase.AuthenticateEmailUseCase
import org.sopt.official.feature.auth.component.AuthHeader
import org.sopt.official.feature.auth.model.EmailAuthenticationState
import org.sopt.official.feature.destinations.EmailSendScreenDestination
import org.sopt.official.feature.destinations.NoticeScreenDestination

@AuthNavGraph(start = true)
@Destination("email")
@Composable
fun EmailInputScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    SoptTheme {
        val systemUiController = rememberSystemUiController()
        var isEmailFieldFocused by remember { mutableStateOf(false) }

        SideEffect {
            systemUiController.setStatusBarColor(
                color = White,
                darkIcons = false
            )
        }

        LaunchedEffect(viewModel.emailAuthenticationState) {
            when (viewModel.emailAuthenticationState) {
                EmailAuthenticationState.SUCCESS -> {
                    navigator.navigate(EmailSendScreenDestination.route)
                }

                EmailAuthenticationState.FAIL -> {
                    Toast.makeText(context, "이메일 인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Toolbar(label = "인증하기") {
                navigator.popBackStack()
                viewModel.onPressAuthenticate()
                navigator.navigate(EmailSendScreenDestination.route)
            }
            Spacer(modifier = Modifier.height(68.dp))
            AuthHeader(
                containerModifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                emphasizedTitleLabel = "이메일",
                extraTitleLabel = "을 입력해주세요",
                contentLabel = "SOPT 지원 시 사용했던 이메일을 입력하면\n" + "회원인증을 할 수 있어요"
            )
            Text(
                text = "SOPT 회원이 아니에요 >",
                style = SoptTheme.typography.caption,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navigator.popBackStack()
                        navigator.navigate(NoticeScreenDestination.route)
                    },
                color = SoptTheme.colors.onSurface40
            )
            TextField(
                value = viewModel.email,
                onValueChange = { viewModel.onChangeEmail(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .onFocusChanged {
                        isEmailFieldFocused = it.isFocused
                    },
                singleLine = true,
                textStyle = SoptTheme.typography.b1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = SoptTheme.colors.onSurface90,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = SoptTheme.colors.onSurface50,
                    unfocusedIndicatorColor = SoptTheme.colors.onSurface50,
                    errorIndicatorColor = SoptTheme.colors.error
                ),
                label = {
                    Text(
                        text = "이메일 입력",
                        style = SoptTheme.typography.b1,
                        color = if (!isEmailFieldFocused) SoptTheme.colors.onSurface30 else Color.Transparent
                    )
                },
                isError = viewModel.isEmailInvalid,
            )
            if (viewModel.isEmailInvalid) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "이메일 형식이 올바르지 않습니다",
                        style = SoptTheme.typography.caption,
                        color = SoptTheme.colors.error
                    )
                }
            }
        }
    }
}

@Composable
private fun Toolbar(
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = label,
            style = SoptTheme.typography.b1,
            color = SoptTheme.colors.primary,
            modifier = Modifier
                .padding(16.dp)
                .clickable { onClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmailInputScreenPreview() {
    EmailInputScreen(
        navigator = EmptyDestinationsNavigator,
        viewModel = AuthViewModel(
            AuthenticateEmailUseCase(
                object : AuthRepository {
                    override suspend fun authenticateEmail(email: String, clientToken: String) = -1L
                    override fun saveUserToken(userId: Long) = Unit
                }
            )
        )
    )
}
