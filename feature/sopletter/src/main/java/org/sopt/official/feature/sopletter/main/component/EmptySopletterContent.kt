package org.sopt.official.feature.sopletter.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.sopletter.R

@Composable
internal fun EmptySopletterContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_sopletter_empty),
            contentDescription = null,
        )

        Text(
            text = "작성된 솝레터가 없어요.\n우리 기수 첫 번쨰 솝레터의 주인공은?",
            style = SoptTheme.typography.body18M,
            color = SoptTheme.colors.onSurface200,
            textAlign = TextAlign.Center,
        )
    }
}
