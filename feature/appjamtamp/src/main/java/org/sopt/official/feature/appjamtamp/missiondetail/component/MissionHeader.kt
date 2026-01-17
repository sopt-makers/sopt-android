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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.component.LevelOfMission
import org.sopt.official.feature.appjamtamp.model.Stamp

@Composable
fun MissionHeader(
    title: String,
    stamp: Stamp
) {
    val backgroundColor = SoptTheme.colors.onSurface800

    Surface(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp),
            )
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LevelOfMission(
                stamp = stamp,
                spaceSize = 10.dp
            )
            Text(
                text = title,
                style = SoptTheme.typography.body16M,
                color = SoptTheme.colors.primary,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    SoptTheme {
        MissionHeader(
            title = "오늘은 무슨 일을 할까?",
            stamp = Stamp.LEVEL2
        )
    }
}

@Preview
@Composable
private fun HeaderPreview2() {
    SoptTheme {
        MissionHeader(
            title = "오늘은 무슨 일을 할까?",
            stamp = Stamp.LEVEL10
        )
    }
}
