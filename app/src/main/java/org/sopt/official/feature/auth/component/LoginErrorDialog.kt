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
package org.sopt.official.feature.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import org.sopt.official.R
import org.sopt.official.designsystem.Black
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray700
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun LoginErrorDialog(
    onDismissRequest: () -> Unit,
    onFindAccountClick: () -> Unit,
    onResetAccountClick: () -> Unit,
    onContactChannelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Popup {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black.copy(alpha = 0.5f))
                .clickable(onClick = onDismissRequest)
        ) {
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray800)
                    .padding(8.dp)
            ) {
                DialogTitle()
                Spacer(modifier = Modifier.height(12.dp))
                LoginDialogText(
                    text = "로그인한 계정을 알고 싶어요.",
                    onClick = onFindAccountClick
                )
                Spacer(modifier = Modifier.height(4.dp))
                LoginDialogText(
                    text = "소셜 계정을 재설정하고 싶어요.",
                    onClick = onResetAccountClick
                )
                Spacer(modifier = Modifier.height(4.dp))
                LoginDialogText(
                    text = "카카오톡 채널에 문의할게요.",
                    onClick = onContactChannelClick
                )
            }
        }
    }
}

@Composable
private fun DialogTitle() {
    Row(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_auth_alert_circle),
            contentDescription = "로그인 에러 아이콘",
            tint = Gray10,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = "로그인이 안 되나요?",
            color = Gray10,
            style = SoptTheme.typography.title20SB
        )
    }
}

@Composable
private fun LoginDialogText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if (isPressed) Gray700 else Gray800

    Text(
        text = text,
        color = Gray10,
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick()
            }
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(10.dp),
        style = SoptTheme.typography.body16R,
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginErrorDialogPreview() {
    SoptTheme {
        LoginErrorDialog(
            onDismissRequest = {},
            onFindAccountClick = {},
            onResetAccountClick = {},
            onContactChannelClick = {}
        )
    }
}
