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
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package org.sopt.official.feature.poke.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.poke.R

@Composable
fun OnboardingScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(SoptTheme.colors.background),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "콕 찌르기",
                        style = SoptTheme.typography.h6,
                        color = SoptTheme.colors.onSurface
                    )
                },
                navigationIcon = {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_close),
                        contentDescription = "Close Screen"
                    )
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(SoptTheme.colors.background)
        ) {

        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    SoptTheme {
        OnboardingScreen()
    }
}
