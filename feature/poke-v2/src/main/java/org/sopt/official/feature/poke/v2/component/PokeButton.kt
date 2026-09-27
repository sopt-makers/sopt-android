package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.mds.theme.SoptTheme

private val PokeButtonSize = 44.dp

/**
 * 콕 찌르기 버튼
 *
 * @param enabled  false 이면 비활성 아이콘 표시 + 탭 불가 (이미 찌른 유저)
 * @param onClick  탭 시 호출
 */
@Composable
internal fun PokeButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pokeButtonImage = if (enabled) R.drawable.ic_poke_enable else R.drawable.ic_poke_disable
    Image(
        imageVector = ImageVector.vectorResource(id = pokeButtonImage),
        contentDescription = null,
        modifier = modifier
            .size(PokeButtonSize)
            .noRippleClickable(
                enabled = enabled,
                onClick = onClick,
            ),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1012)
@Composable
private fun PokeButtonPreview() {
    SoptTheme {
        Row {
            PokeButton(enabled = true, onClick = {})
            PokeButton(enabled = false, onClick = {})
        }
    }
}
