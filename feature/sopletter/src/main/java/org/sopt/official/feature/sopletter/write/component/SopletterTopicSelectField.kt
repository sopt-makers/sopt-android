package org.sopt.official.feature.sopletter.write.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun SopletterTopicSelectField(
    selectedTopicTitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = selectedTopicTitle ?: "주제를 선택해 주세요.",
        style = SoptTheme.typography.body1,
        color = if (selectedTopicTitle != null) SoptTheme.colors.fg.neutral.bold else SoptTheme.colors.fg.neutral.subtle,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.bg.neutral.ghost,
                shape = RoundedCornerShape(SoptTheme.radius.r12),
            )
            .noRippleClickable(onClick = onClick)
            .padding(
                horizontal = SoptTheme.spacing.s20,
                vertical = SoptTheme.spacing.s10,
            ),
    )
}

@Preview
@Composable
private fun SopletterTopicSelectFieldPreview() {
    SoptTheme {
        SopletterTopicSelectField(
            selectedTopicTitle = null,
            onClick = {},
        )
    }
}
