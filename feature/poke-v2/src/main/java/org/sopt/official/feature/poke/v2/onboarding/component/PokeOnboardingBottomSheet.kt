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
package org.sopt.official.feature.poke.v2.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.mds.components.button.MdsActionButton
import org.sopt.official.mds.components.button.MdsActionButtonSize
import org.sopt.official.mds.components.button.MdsActionButtonType
import org.sopt.official.mds.theme.SoptTheme
import org.sopt.official.designsystem.SoptTheme as LegacySoptTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokeOnboardingBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SoptTheme.colors.bg.neutral.ghost,
        shape = RoundedCornerShape(
            topStart = SoptTheme.radius.r20,
            topEnd = SoptTheme.radius.r20,
        ),
        dragHandle = null,
    ) {
        PokeOnboardingBottomSheetContent(
            onConfirmClick = onDismissRequest,
            modifier = modifier
        )
    }
}

@Composable
private fun PokeOnboardingBottomSheetContent(
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = SoptTheme.spacing.s16,
                end = SoptTheme.spacing.s16,
                top = SoptTheme.spacing.s24,
                bottom = SoptTheme.spacing.s32,
            ),
    ) {
        Text(
            text = "솝트에 콕 찌르기가 생겼어요!",
            style = SoptTheme.typography.title3,
            color = SoptTheme.colors.fg.neutral.bold,
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Image(
            painter = painterResource(R.drawable.img_onboarding),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = "서로 찌르면 친구가 될 수 있어요. 친구와 자주 찌를수록 \n전송할 수 있는 메시지 문구가 다양해져요.",
            style = SoptTheme.typography.body2,
            color = SoptTheme.colors.fg.neutral.bold,
        )

        Spacer(modifier = Modifier.padding(20.dp))

        MdsActionButton(
            text = "확인",
            type = MdsActionButtonType.PRIMARY,
            size = MdsActionButtonSize.LARGE,
            onClick = onConfirmClick,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PokeOnboardingBottomSheetPreview() {
    SoptTheme {
        LegacySoptTheme {
            PokeOnboardingBottomSheetContent(
                onConfirmClick = {},
            )
        }
    }
}
