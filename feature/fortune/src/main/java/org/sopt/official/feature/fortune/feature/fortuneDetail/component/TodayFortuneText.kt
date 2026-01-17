/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import android.graphics.Paint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.LineBreak.Companion.Simple
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography

@Composable
fun TodayFortuneText(
    todaySentence: String,
    name: String,
    modifier: Modifier = Modifier,
) {
    val currentDensity = LocalDensity.current
    val currentConfiguration = LocalConfiguration.current
    val textPaint = remember { Paint().apply { textSize = with(currentDensity) { 24.sp.toPx() } } }
    val screenWidth = remember { with(currentDensity) { (currentConfiguration.screenWidthDp.dp - (88.dp * 2)).toPx() } }
    val formattedSentence = remember { todaySentence.toLineBreakingSentence(textPaint, screenWidth) }

    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 68.dp)
            .semantics { contentDescription = "todaySentence" },
    ) {
        Text(
            text = "${name}님,",
            style = typography.title24SB,
            color = colors.onSurface30,
            maxLines = 1,
            textAlign = Center,
        )
        Spacer(modifier = Modifier.height(height = 6.dp))
        Text(
            text = formattedSentence,
            style = typography.title24SB.copy(
                lineBreak = Simple,
            ),
            color = colors.onSurface30,
            maxLines = 4,
            textAlign = Center,
            softWrap = true,
        )
    }
}

private fun String.toLineBreakingSentence(textPaint: Paint, screenWidth: Float): String {
    var resultSentence = ""
    var sentenceLineWidth = 0f
    val words = split(" ")
    val spaceWidth = textPaint.measureText(" ")

    words.forEach { word ->
        val wordWidth = textPaint.measureText(word)

        when (sentenceLineWidth + wordWidth + spaceWidth > screenWidth) {
            true -> {
                resultSentence += "\n$word"
                sentenceLineWidth = wordWidth
            }

            false -> {
                resultSentence = if (resultSentence.isBlank()) word else "$resultSentence $word"
                sentenceLineWidth += wordWidth + spaceWidth
            }
        }
    }

    return resultSentence
}

@Preview
@Composable
private fun TodayFortuneTextPreview() {
    SoptTheme {
        TodayFortuneText(
            todaySentence = "저 근데 진짜 발 딛는 것도 처음이라 좀 수치스러울 것 같은데 옆에서 카공하다",
            name = "김세훈",
        )
    }
}
