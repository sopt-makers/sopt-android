/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.SoptTheme

@Composable
fun ClapFeedbackHolder(
    clapCount: Int,
    myClapCount: Int?,
    onPressClap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isBadgeVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(bottom = 30.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        ClapButton(
            clapCount = clapCount,
            myClapCount = myClapCount,
            onClicked = {
                onPressClap()
                coroutineScope.launch {
                    isBadgeVisible = true
                    delay(500L)
                    isBadgeVisible = false
                }
            },
        )

        AnimatedVisibility(
            visible = isBadgeVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 }
            ) + fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .padding(bottom = 64.dp)
        ) {
            BadgeClap(myClapCount = myClapCount ?: 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClapFeedbackHolderPreview() {
    SoptTheme {
        ClapFeedbackHolder(
            clapCount = 10,
            myClapCount = 5,
            onPressClap = {}
        )
    }
}
