package org.sopt.official.feature.poke.v2.friend.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.sopt.official.feature.poke.v2.friend.model.PokeRelationChangeState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun PokeRelationChangeOverlay(
    relationChange: PokeRelationChangeState,
    onRelationChangeLottieEnd: () -> Unit,
    onSoulmateRevealEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (relationChange) {
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
