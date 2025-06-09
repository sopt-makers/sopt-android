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
package org.sopt.official.feature.auth.feature.authmain

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.delay
import org.sopt.official.R
import org.sopt.official.common.view.toast
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray50
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.AuthNavigationText
import org.sopt.official.feature.auth.model.AuthStatus

@Composable
internal fun AuthMainRoute(
    platform: String,
    navigateToUnAuthenticatedHome: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    navigateToCertification: (AuthStatus) -> Unit,
    onContactChannelClick: () -> Unit,
    viewModel: AuthMainViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    var loginDialogVisibility by remember { mutableStateOf(false) }

    if (loginDialogVisibility) {
        LoginErrorDialog(
            onDismissRequest = {
                loginDialogVisibility = false
            },
            onFindAccountClick = {
                navigateToCertification(AuthStatus.SEARCH_SOCIAL_PLATFORM)
                loginDialogVisibility = false
            },
            onResetAccountClick = {
                navigateToCertification(AuthStatus.CHANGE_SOCIAL_PLATFORM)
                loginDialogVisibility = false
            },
            onContactChannelClick = {
                onContactChannelClick()
                loginDialogVisibility = false
            }
        )
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is AuthMainSideEffect.ShowToast -> context.toast(sideEffect.message)
                }
            }
    }

    AuthMainScreen(
        platform = platform,
        showDialog = {
            loginDialogVisibility = true
        },
        onGoogleLoginCLick = onGoogleLoginCLick,
        onLoginLaterClick = navigateToUnAuthenticatedHome,
        navigateToCertification = {
            navigateToCertification(AuthStatus.REGISTER)
        }
    )
}

@Composable
private fun AuthMainScreen(
    platform: String,
    showDialog: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    onLoginLaterClick: () -> Unit,
    navigateToCertification: () -> Unit
) {
    var showAuthBottom by remember { mutableStateOf(false) }
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(700)
        showAuthBottom = true
        offsetY.animateTo(
            targetValue = -140f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.img_logo),
                contentDescription = "솝트 로고",
                modifier = Modifier.offset(y = offsetY.value.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        AnimatedVisibility(
            visible = showAuthBottom,
            enter = fadeIn(
                initialAlpha = 0.1f,
                animationSpec = tween(easing = EaseIn)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            AuthFooter(
                platform = platform,
                showDialog = showDialog,
                onGoogleLoginCLick = onGoogleLoginCLick,
                onLoginLaterClick = onLoginLaterClick,
                navigateToCertification = navigateToCertification
            )
        }
    }
}

@Composable
private fun AuthFooter(
    platform: String,
    showDialog: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    onLoginLaterClick: () -> Unit,
    navigateToCertification: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (platform.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = SoptTheme.colors.success)
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                text = "로그인한 계정은 ${platform}이에요.",
                color = Gray50,
                style = SoptTheme.typography.body13M
            )
            Image(
                painter = painterResource(R.drawable.ic_auth_platform),
                contentDescription = "화살표"
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        AuthButton(
            padding = PaddingValues(vertical = 12.dp),
            onClick = onGoogleLoginCLick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_auth_google),
                contentDescription = "구글 로고",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "Google로 로그인",
                style = SoptTheme.typography.label16SB
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AuthNavigationText(
            text = "로그인이 안 되나요?",
            modifier = Modifier.clickable(onClick = showDialog)
        )
        Spacer(modifier = Modifier.height(44.dp))
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(
                thickness = Dp.Hairline,
                color = Gray300,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "또는",
                color = Gray300,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(
                thickness = Dp.Hairline,
                color = Gray300,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AuthButton(
            padding = PaddingValues(vertical = 12.dp),
            onClick = navigateToCertification,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            containerColor = Gray700,
            contentColor = White
        ) {
            Text(
                text = "SOPT 회원가입",
                style = SoptTheme.typography.label16SB
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AuthNavigationText(
            text = "나중에 로그인할래요.",
            modifier = Modifier.clickable(onClick = onLoginLaterClick)
        )
        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
internal fun LoginErrorDialog(
    onDismissRequest: () -> Unit,
    onFindAccountClick: () -> Unit,
    onResetAccountClick: () -> Unit,
    onContactChannelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismissRequest)
        ) {
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray800)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_auth_alert_circle),
                        contentDescription = "로그인 에러 아이콘",
                        tint = Gray10,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Text(
                        text = "로그인이 안 되나요?",
                        color = Gray10,
                        style = SoptTheme.typography.title20SB
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                LoginDialogText(
                    text = "로그인한 계정을 알고 싶어요.",
                    onClick = onFindAccountClick
                )
                Spacer(modifier = Modifier.height(4.dp))
                LoginDialogText(
                    text = "소셜 계정을 재설정하고 싶어요.",
                    onClick = onResetAccountClick
                )
                Spacer(modifier = Modifier.height(4.dp))
                LoginDialogText(
                    text = "카카오톡 채널에 문의할게요.",
                    onClick = onContactChannelClick
                )
            }
        }
    }
}

@Composable
private fun LoginDialogText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if (isPressed) Gray700 else Gray800

    Text(
        text = text,
        color = Gray10,
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick()
            }
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(10.dp),
        style = SoptTheme.typography.body16R,
    )
}

@Preview(showBackground = true)
@Composable
private fun AuthMainScreenPreview() {
    SoptTheme {
        AuthMainScreen(
            platform = "",
            showDialog = {},
            onGoogleLoginCLick = {},
            onLoginLaterClick = {},
            navigateToCertification = {}
        )
    }
}
