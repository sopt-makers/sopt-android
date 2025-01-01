package org.sopt.official.feature.auth.feature.authmain

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import org.sopt.official.R
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.auth.AuthViewModel
import org.sopt.official.feature.auth.component.AuthButton
import org.sopt.official.feature.auth.component.AuthTextWithArrow
import org.sopt.official.feature.auth.component.LoginErrorDialog

@Composable
internal fun AuthMainRoute(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToUnAuthenticatedHome: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    navigateToCertification: () -> Unit
) {
    var loginDialogVisibility by remember { mutableStateOf(false) }

    if (loginDialogVisibility) {
        LoginErrorDialog(
            onDismissRequest = {
                loginDialogVisibility = false
            }
        )
    }

    AuthMainScreen(
        showDialog = {
            loginDialogVisibility = true
        },
        onGoogleLoginCLick = {
            onGoogleLoginCLick()
        },
        onLoginLaterClick = {
            navigateToUnAuthenticatedHome()
        },
        navigateToCertification = navigateToCertification
    )
}

@Composable
private fun AuthMainScreen(
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
            AuthBottom(
                showDialog = showDialog,
                onGoogleLoginCLick = onGoogleLoginCLick,
                onLoginLaterClick = onLoginLaterClick,
                navigateToCertification = navigateToCertification
            )
        }
    }
}

@Composable
private fun AuthBottom(
    showDialog: () -> Unit,
    onGoogleLoginCLick: () -> Unit,
    onLoginLaterClick: () -> Unit,
    navigateToCertification: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        AuthTextWithArrow(
            text = "로그인이 안 되나요?",
            modifier = Modifier.clickable(onClick = showDialog)
        )
        Spacer(modifier = Modifier.height(44.dp))
        AuthDivider()
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
        AuthTextWithArrow(
            text = "나중에 로그인할래요.",
            modifier = Modifier.clickable(onClick = onLoginLaterClick)
        )
        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun AuthDivider() {
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
}

@Preview(showBackground = true)
@Composable
private fun AuthMainScreenPreview() {
    SoptTheme {
        AuthMainScreen(
            showDialog = {},
            onGoogleLoginCLick = {},
            onLoginLaterClick = {},
            navigateToCertification = {}
        )
    }
}