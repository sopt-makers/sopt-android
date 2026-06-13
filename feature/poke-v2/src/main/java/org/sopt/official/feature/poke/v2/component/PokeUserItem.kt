package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import org.sopt.official.common.util.throttledNoRippleClickable
import org.sopt.official.designsystem.Blue400
import org.sopt.official.designsystem.Green400
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.main.model.PokeViewType

private val PokeUserUiState.relationStrokeColor: Color get() = when (relationName) {
        PokeFriendType.NEW.readableName -> Blue400
        PokeFriendType.BEST_FRIEND.readableName -> Green400
        PokeFriendType.SOULMATE.readableName -> Orange400
        else -> Color.Transparent
    }

@Composable
internal fun PokeUserItem(
    modifier: Modifier = Modifier,
    user: PokeUserUiState,
    pokeViewType: PokeViewType = PokeViewType.SMALL,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit
) {
    when (pokeViewType) {
        PokeViewType.SMALL -> PokeUserSmallItem(
            modifier = modifier,
            user = user,
            onProfileClick = onProfileClick,
            onPokeClick = onPokeClick
        )
        PokeViewType.LARGE -> PokeUserLargeItem(
            modifier = modifier,
            user = user,
            onProfileClick = onProfileClick,
            onPokeClick = onPokeClick
        )
    }
}

@Composable
private fun PokeUserLargeItem(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            PokeProfileImage(
                user = user,
                onProfileClick = onProfileClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .aspectRatio(1f)
            )

            PokeButton(
                enabled = user.isPokeButtonEnabled,
                onClick = { onPokeClick(user) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth(0.37f)
                    .aspectRatio(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = user.displayName,
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.onSurface30,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        if (!user.isAnonymous) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.infoText,
                style = SoptTheme.typography.label12SB,
                color = SoptTheme.colors.onSurface300,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PokeUserSmallItem(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        PokeProfileImage(
            user = user,
            onProfileClick = onProfileClick,
            modifier = Modifier
                .size(50.dp)
                .border(width = 2.dp, color = user.relationStrokeColor, shape = CircleShape),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = user.displayName,
            style = SoptTheme.typography.heading16B,
            color = SoptTheme.colors.onSurface30,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (user.isAnonymous) {
            Spacer(modifier = Modifier.weight(1f))
        } else {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = user.infoText,
                style = SoptTheme.typography.label12SB,
                color = SoptTheme.colors.onSurface300,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "${user.pokeCount}콕",
            style = SoptTheme.typography.body16M,
            color = SoptTheme.colors.onSurface30,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        PokeButton(
            enabled = user.isPokeButtonEnabled,
            onClick = { onPokeClick(user) },
            modifier = Modifier.size(44.dp)
        )
    }
}

@Composable
private fun PokeProfileImage(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageModifier = modifier.clip(CircleShape)
    val clickableImageModifier = if (user.isAnonymous) {
        imageModifier
    } else {
        imageModifier.throttledNoRippleClickable(onClick = { onProfileClick(user.userId) })
    }

    if (user.isAnonymous) {
        Image(
            painter = painterResource(id = R.drawable.image_anonymous_profile),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = clickableImageModifier
        )
    } else {
        AsyncImage(
            modifier = clickableImageModifier,
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(user.profileImageUrl)
                .placeholder(drawableResId = R.drawable.ic_empty_profile)
                .error(drawableResId = R.drawable.ic_empty_profile)
                .fallback(drawableResId = R.drawable.ic_empty_profile)
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun PokeButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pokeButtonImage = if (enabled) R.drawable.ic_poke_enable else R.drawable.ic_poke_disable
    Image(
        imageVector = ImageVector.vectorResource(id = pokeButtonImage),
        contentDescription = "콕 찌르기",
        modifier = modifier
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    )
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PokeUserSmallItemPreview(
    @PreviewParameter(PokeUserSmallPreviewParameterProvider::class) user: PokeUserUiState
) {
    SoptTheme {
        PokeUserItem(
            user = user,
            pokeViewType = PokeViewType.SMALL, // 외부에서 SMALL 주입
            onProfileClick = {},
            onPokeClick = {}
        )
    }
}

private class PokeUserSmallPreviewParameterProvider : PreviewParameterProvider<PokeUserUiState> {
    override val values = sequenceOf(
        PokeUserUiState(
            userId = 1,
            userName = "김솝트",
            anonymousName = "수상한 솝트인",
            userGeneration = 36,
            userPart = "안드로이드",
            profileImageUrl = null,
            pokeCount = 4,
            relationName = PokeFriendType.NEW.readableName,
            isAnonymous = true,
            isPokeButtonEnabled = false
        ),
        PokeUserUiState(
            userId = 2,
            userName = "김솝트",
            anonymousName = "수상한 솝트인",
            userGeneration = 36,
            userPart = "안드로이드",
            profileImageUrl = null,
            pokeCount = 9,
            relationName = PokeFriendType.SOULMATE.readableName,
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)@Composable
fun PokeUserLargeItemPreview(
    @PreviewParameter(PokeUserLargePreviewParameterProvider::class) user: PokeUserUiState
) {
    SoptTheme {
        PokeUserItem(
            user = user,
            pokeViewType = PokeViewType.LARGE, // 외부에서 LARGE 주입
            onProfileClick = {},
            onPokeClick = {}
        )
    }
}

private class PokeUserLargePreviewParameterProvider : PreviewParameterProvider<PokeUserUiState> {
    override val values = sequenceOf(
        PokeUserUiState(
            userId = 3,
            userName = "박메이커",
            anonymousName = "익명의 메이커",
            userGeneration = 36,
            userPart = "기획",
            profileImageUrl = null,
            relationName = PokeFriendType.SOULMATE.readableName,
            isAnonymous = true,
            isPokeButtonEnabled = false
        ),
        PokeUserUiState(
            userId = 4,
            userName = "커비",
            anonymousName = "익명의 메이커",
            userGeneration = 38,
            userPart = "디자인",
            profileImageUrl = null,
            relationName = PokeFriendType.SOULMATE.readableName,
            isAnonymous = false,
            isPokeButtonEnabled = true
        )
    )
}
