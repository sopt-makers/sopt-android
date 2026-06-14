package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.poke.v2.R

@Composable
internal fun PokeFriendListEmpty(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_poke_empty),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 18.dp)
                .size(width = 64.dp, height = 62.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "아직 없어요 T.T\n더 많은 찌르기로 달성해보세요",
            style = SoptTheme.typography.body14R,
            color = SoptTheme.colors.onSurface300,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PokeFriendListEmptyPreview() {
    SoptTheme {
        PokeFriendListEmpty(
            modifier = Modifier.fillMaxWidth()
        )
    }
}
