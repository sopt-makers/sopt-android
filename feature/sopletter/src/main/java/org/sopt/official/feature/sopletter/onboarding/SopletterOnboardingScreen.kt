/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.sopletter.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.common.component.SopletterButton
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarType
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarVisuals
import org.sopt.official.feature.sopletter.onboarding.component.SopletterOnboardingInfoHolder
import org.sopt.official.sopletter.R

@Composable
fun SopletterOnboardingRoute(
    navigateToNickname: (String, Int) -> Unit,
    navigateToHome: () -> Unit,
    onShowSnackbar: (SopletterSnackbarVisuals) -> Unit,
    viewModel: SopletterOnboardingViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    is SopletterOnboardingSideEffect.ShowSnackbar -> {
                        onShowSnackbar(
                            SopletterSnackbarVisuals(
                                message = sideEffect.message,
                                type = SopletterSnackbarType.FAILURE,
                            )
                        )
                    }

                    is SopletterOnboardingSideEffect.NavigateToNickname -> {
                        navigateToNickname(sideEffect.nickname, sideEffect.currentGeneration)
                    }
                }
            }
        }
    }

    SopletterOnboardingScreen(
        navigateToNickname = viewModel::updateSopletterOnboardingStatus,
        navigateToHome = navigateToHome,
    )
}

@Composable
private fun SopletterOnboardingScreen(
    navigateToNickname: () -> Unit,
    navigateToHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.onSurface950),
    ) {
        SopletterTopbar(
            onBackClick = navigateToHome,
            topbarTitle = "",
            iconRes = R.drawable.ic_close_32
        )

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
            navigateToNickname = {},
            navigateToHome = {}
        )
    }
}
