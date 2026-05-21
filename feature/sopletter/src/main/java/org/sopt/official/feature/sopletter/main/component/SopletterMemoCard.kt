package org.sopt.official.feature.sopletter.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.main.model.SopletterMemoUiModel
import org.sopt.official.sopletter.R

@Composable
internal fun SopletterMemoCard(
    memo: SopletterMemoUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .rotate(memo.rotation.degree)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(memo.shapeImageRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(memo.memoColor.color),
        )

        Text(
            text = memo.message,
            modifier = Modifier
                .width(111.dp)
                .height(110.dp),
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.onSurface800,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
