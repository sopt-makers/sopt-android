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
package org.sopt.official.feature.fortune.feature.fortundDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun FortuneDetailRoute(
    paddingValue: PaddingValues,
    date: String,
    navigateToFortuneAmulet: () -> Unit,
) {
    FortuneDetailScreen(
        paddingValue = paddingValue,
        date = date,
        navigateToFortuneAmulet = navigateToFortuneAmulet
    )
}

@Composable
fun FortuneDetailScreen(
    paddingValue: PaddingValues,
    date: String,
    navigateToFortuneAmulet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(paddingValue)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fortune Detail Screen: $date",
            color = SoptTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = navigateToFortuneAmulet
        ) {
            Text(text = "Go to Fortune Amulet")
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}


@Preview
@Composable
fun FortuneDetailScreenPreview() {
    SoptTheme {
        FortuneDetailScreen(
            paddingValue = PaddingValues(16.dp),
            date = "2024-09-09",
            navigateToFortuneAmulet = {}
        )
    }
}
