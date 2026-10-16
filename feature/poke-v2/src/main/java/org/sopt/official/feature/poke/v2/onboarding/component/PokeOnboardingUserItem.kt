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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.component.PokeProfileCard
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun PokeOnboardingUserItem(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    PokeProfileCard(
        user = user,
        onProfileClick = onProfileClick,
        onPokeClick = onPokeClick,
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1012)
@Composable
private fun PokeOnboardingUserItemPreview() {
    SoptTheme {
        Row {
            PokeOnboardingUserItem(
                user = PokeUserUiState(
                    userId = 1,
                    userName = "커비",
                    userGeneration = 38,
                    userPart = "디자인",
                    profileImageUrl = null,
                    relationName = PokeFriendType.NEW.readableName,
                ),
                onProfileClick = {},
                onPokeClick = {},
                modifier = Modifier.width(154.dp),
            )
            PokeOnboardingUserItem(
                user = PokeUserUiState(
                    userId = 2,
                    userName = "박메이커",
                    userGeneration = 36,
                    userPart = "기획",
                    profileImageUrl = null,
                    relationName = PokeFriendType.SOULMATE.readableName,
                    isPokeButtonEnabled = false,
                ),
                onProfileClick = {},
                onPokeClick = {},
                modifier = Modifier.width(154.dp),
            )
        }
    }
}
