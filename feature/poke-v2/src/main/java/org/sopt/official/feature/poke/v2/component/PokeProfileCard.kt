package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.theme.SoptTheme

private val AvatarSize = 120.dp

/**
 * 찌르기 대상 유저 프로필 카드
 *
 * @param user            표시할 유저
 * @param onProfileClick  프로필 탭 시 userId 전달
 * @param onPokeClick     찌르기 버튼 탭 시 유저 전달
 */
@Composable
internal fun PokeProfileCard(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Box {
            PokeProfileAvatar(
                imageUrl = user.profileImageUrl,
                size = AvatarSize,
                onClick = { onProfileClick(user.userId) },
            )
            PokeButton(
                enabled = user.isPokeButtonEnabled,
                onClick = { onPokeClick(user) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = (-3).dp),
            )
        }

        Spacer(modifier = Modifier.height(SoptTheme.spacing.s10))

        Text(
            text = user.displayName,
            style = SoptTheme.typography.label3,
            color = SoptTheme.colors.fg.neutral.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(SoptTheme.spacing.s4))
        Text(
            text = user.generationPartText,
            style = SoptTheme.typography.label4,
            color = SoptTheme.colors.fg.neutral.subtle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1012)
@Composable
private fun PokeProfileCardPreview() {
    SoptTheme {
        Row {
            PokeProfileCard(
                user = PokeUserUiState(
                    userId = 1,
                    userName = "커비",
                    userGeneration = 38,
                    userPart = "디자인",
                    profileImageUrl = null,
                    relationName = PokeFriendType.SOULMATE.readableName,
                ),
                onProfileClick = {},
                onPokeClick = {},
                modifier = Modifier.width(154.dp),
            )
            PokeProfileCard(
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
                modifier = Modifier.width(130.dp),
            )
        }
    }
}
