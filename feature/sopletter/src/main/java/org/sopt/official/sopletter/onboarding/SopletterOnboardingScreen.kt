package org.sopt.official.sopletter.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.sopletter.component.SopletterButton
import org.sopt.official.sopletter.onboarding.component.SopletterOnboardingInfoHolder

@Composable
fun SopletterOnboardingRoute(
    paddingValues: PaddingValues,
    navigateToNickname: () -> Unit,
) {
    SopletterOnboardingScreen(
        paddingValues = paddingValues,
        navigateToNickname = navigateToNickname,
    )
}

@Composable
private fun SopletterOnboardingScreen(
    paddingValues: PaddingValues,
    navigateToNickname: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.onSurface950)
            .padding(paddingValues),
    ) {
        // Todo : 탑바

        Spacer(modifier = Modifier.weight(58f))

        SopletterOnboardingInfoHolder(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
        )

        Spacer(modifier = Modifier.weight(140f))

        SopletterButton(
            buttonText = "솝레터 시작하기",
            onClick = navigateToNickname,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(40f))
    }
}

@Preview
@Composable
private fun SopletterOnboardingScreenPreview() {
    SoptTheme {
        SopletterOnboardingScreen(
            paddingValues = PaddingValues(),
            navigateToNickname = {},
        )
    }
}