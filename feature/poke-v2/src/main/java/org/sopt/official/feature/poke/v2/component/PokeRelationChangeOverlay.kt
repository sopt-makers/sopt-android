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
package org.sopt.official.feature.poke.v2.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.model.PokeRelationChangeState
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun PokeRelationChangeOverlay(
    relationChange: PokeRelationChangeState,
    onRelationChangeLottieEnd: () -> Unit,
    onSoulmateRevealEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (relationChange) {
        is PokeRelationChangeState.Friend -> PokeCelebrationLottie(
            lottieRes = R.raw.friendcomplete,
            message = "찌르기 답장으로\n${relationChange.user.anonymousName}님과 친구가 되었어요!",
            onAnimationEnd = onRelationChangeLottieEnd
        )

        is PokeRelationChangeState.BestFriend -> PokeRelationChangeLottie(
            type = PokeRelationChangeType.BEST_FRIEND,
            anonymousName = relationChange.user.anonymousName,
            hint = "${relationChange.user.userGeneration}기 ${relationChange.user.userPart}파트",
            onAnimationEnd = onRelationChangeLottieEnd,
            modifier = modifier,
        )

        is PokeRelationChangeState.Soulmate -> PokeRelationChangeLottie(
            type = PokeRelationChangeType.SOULMATE,
            anonymousName = relationChange.user.anonymousName,
            onAnimationEnd = onRelationChangeLottieEnd,
            modifier = modifier,
        )

        is PokeRelationChangeState.SoulmateRevealed -> PokeSoulmateReveal(
            user = relationChange.user,
            onFinish = onSoulmateRevealEnd,
            modifier = modifier,
        )
    }
}

private class RelationChangeStatePreviewParameterProvider : PreviewParameterProvider<PokeRelationChangeState> {
    private val user = PokeUserUiState(
        userId = 1,
        userName = "김솝트",
        anonymousName = "익명의 사자",
        userGeneration = 36,
        userPart = "디자인",
        profileImageUrl = null,
        pokeCount = 11,
        isAnonymous = true,
    )

    override val values = sequenceOf(
        PokeRelationChangeState.BestFriend(user.copy(pokeCount = 5)),
        PokeRelationChangeState.Soulmate(user),
        PokeRelationChangeState.SoulmateRevealed(user),
    )
}

@Preview
@Composable
private fun PokeRelationChangeOverlayPreview(
    @PreviewParameter(RelationChangeStatePreviewParameterProvider::class) relationChange: PokeRelationChangeState,
) {
    SoptTheme {
        PokeRelationChangeOverlay(
            relationChange = relationChange,
            onRelationChangeLottieEnd = {},
            onSoulmateRevealEnd = {},
        )
    }
}
