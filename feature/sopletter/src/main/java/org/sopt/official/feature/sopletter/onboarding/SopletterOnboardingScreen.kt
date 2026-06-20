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
import org.sopt.official.feature.sopletter.common.component.SopletterButton
import org.sopt.official.feature.sopletter.onboarding.component.SopletterOnboardingInfoHolder

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
