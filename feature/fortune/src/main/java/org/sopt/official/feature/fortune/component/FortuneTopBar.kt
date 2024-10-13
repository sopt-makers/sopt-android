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
package org.sopt.official.feature.fortune.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray200
import org.sopt.official.designsystem.SoptTheme

@Composable
fun FortuneTopBar(
    onClickNavigationIcon: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Filled.Close,
            tint = if (!isEnabled) Gray200 else SoptTheme.colors.onBackground,
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp).padding(8.dp)
                .clickable { if (isEnabled) onClickNavigationIcon() },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FortuneTopBarPreview() {
    SoptTheme {
        FortuneTopBar(
            onClickNavigationIcon = { },
            isEnabled = false,
        )
    }
}
