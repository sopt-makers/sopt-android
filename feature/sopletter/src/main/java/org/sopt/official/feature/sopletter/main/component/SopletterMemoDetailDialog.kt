package org.sopt.official.feature.sopletter.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.sopletter.R

@Composable
internal fun SopletterMemoDetailDialog(
    state: SopletterMemoDetailDialogContract.State,
    actions: SopletterMemoDetailDialogContract.Actions,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = actions::onDismissClick,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = modifier
                .padding(20.dp)
                .fillMaxWidth()
                .background(
                    color = state.memoColor.color,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = state.writerName,
                    style = SoptTheme.typography.title16SB,
                    color = SoptTheme.colors.onSurface600,
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            bottom = 4.dp,
                        ),
                )

                when (state.isMine) {
                    true -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_edit_32),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .noRippleClickable(actions::onEditClick),
                            )

                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_trash_32),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .noRippleClickable(actions::onDeleteClick),
                            )
                        }
                    }

                    false -> Unit
                }
            }

            Text(
                text = state.content,
                style = SoptTheme.typography.body16R,
                color = SoptTheme.colors.onSurface600,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.date,
                    style = SoptTheme.typography.body16R,
                    color = SoptTheme.colors.onSurface300,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.noRippleClickable(actions::onLikeClick),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            if (state.isLiked) R.drawable.ic_active_heart_24 else R.drawable.ic_inactive_heart_24
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                    Text(
                        text = "${state.likeCount}",
                        style = SoptTheme.typography.body16R,
                        color = SoptTheme.colors.onSurface600,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = SoptTheme.colors.onSurface800,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .noRippleClickable(actions::onDismissClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "확인",
                    style = SoptTheme.typography.title16SB,
                    color = SoptTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(14.dp),
                )
            }
        }
    }
}
