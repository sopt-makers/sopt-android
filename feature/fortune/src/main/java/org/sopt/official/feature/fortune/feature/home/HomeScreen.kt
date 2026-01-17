/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.fortune.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.R
import org.sopt.official.feature.fortune.component.FortuneButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeRoute(
    onFortuneDetailClick: (String) -> Unit,
) {
    val date = rememberSaveable { getToday() }
    val amplitudeTracker = LocalTracker.current.also {
        it.track(
            type = EventType.VIEW,
            name = "soptmadi_title",
        )
    }

    HomeScreen(
        date = date,
        onFortuneDetailClick = {
            onFortuneDetailClick(date)
            amplitudeTracker.track(
                type = EventType.CLICK,
                name = "check_todaysoptmadi",
            )
        }
    )
}

@Composable
private fun HomeScreen(
    date: String,
    onFortuneDetailClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .background(SoptTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = date,
            style = SoptTheme.typography.body16M,
            color = SoptTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "오늘의 운세가 도착했어요!",
            style = SoptTheme.typography.heading18B,
            color = SoptTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_fortune_title_large),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 25.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_fortune_three_cards),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 6.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        FortuneButton(
            title = "오늘의 운세 확인하기",
            onClick = onFortuneDetailClick,
        )

        Spacer(modifier = Modifier.height(36.dp))
    }
}

fun getToday(): String {
    val today = LocalDate.now()

    val monthDay = today.format(DateTimeFormatter.ofPattern("M월 d일"))
    val dayOfWeek = today.dayOfWeek.getDisplayName(
        TextStyle.FULL,
        Locale.KOREAN
    )

    return "$monthDay $dayOfWeek"
}

@Preview
@Composable
fun HomeScreenPreview() {
    SoptTheme {
        HomeScreen(
            date = getToday()
        )
    }
}
