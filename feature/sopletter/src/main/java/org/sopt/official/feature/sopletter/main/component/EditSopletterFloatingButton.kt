package org.sopt.official.feature.sopletter.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.sopletter.R

@Composable
internal fun EditSopletterFloatingActionButton(
    onEditFABClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
            )
            .noRippleClickable(onEditFABClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_edit_28),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}
