package org.sopt.official.feature.sopletter.write.component

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.domain.sopletter.model.SopletterTopic
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.components.button.MdsActionButton
import org.sopt.official.mds.components.button.MdsActionButtonSize
import org.sopt.official.mds.components.button.MdsActionButtonType
import org.sopt.official.mds.components.tag.MdsTag
import org.sopt.official.mds.components.tag.MdsTagEmphasis
import org.sopt.official.mds.components.tag.MdsTagShape
import org.sopt.official.mds.components.tag.MdsTagSize
import org.sopt.official.mds.components.tag.MdsTagType
import org.sopt.official.mds.theme.SoptTheme

// 주제는 5개까지 보이고 나머지는 스크롤 (아이템 높이 64dp)
private val TOPIC_LIST_MAX_HEIGHT = 320.dp

@Composable
internal fun SopletterTopicSelectDialog(
    topics: ImmutableList<SopletterTopic>,
    selectedTopicId: Long?,
    onDismissRequest: () -> Unit,
    onSelectComplete: (Long) -> Unit,
) {
    var pendingTopicId by remember { mutableStateOf(selectedTopicId) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        DisposableEffect(dialogWindow) {
            dialogWindow?.setGravity(Gravity.BOTTOM)
            onDispose { }
        }

        SopletterTopicSelectContent(
            topics = topics,
            selectedTopicId = pendingTopicId,
            onTopicClick = { pendingTopicId = it },
            onBackClick = onDismissRequest,
            onSelectComplete = onSelectComplete,
            modifier = Modifier.padding(
                start = SoptTheme.spacing.s28,
                end = SoptTheme.spacing.s28,
                bottom = SoptTheme.spacing.s8,
            ),
        )
    }
}

@Composable
private fun SopletterTopicSelectContent(
    topics: ImmutableList<SopletterTopic>,
    selectedTopicId: Long?,
    onTopicClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    onSelectComplete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.bg.neutral.ghost,
                shape = RoundedCornerShape(SoptTheme.radius.r20),
            )
            .padding(vertical = SoptTheme.spacing.s12),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s8),
            modifier = Modifier.padding(
                start = SoptTheme.spacing.s8,
                top = SoptTheme.spacing.s8,
            ),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(MdsIcons.chevronLeftOutlined),
                contentDescription = null,
                tint = SoptTheme.colors.fg.neutral.bold,
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable(onClick = onBackClick),
            )

            Text(
                text = "주제 선택",
                style = SoptTheme.typography.heading3,
                color = SoptTheme.colors.fg.neutral.bold,
            )
        }

        Spacer(modifier = Modifier.height(SoptTheme.spacing.s12))

        LazyColumn(
            modifier = Modifier
                .weight(weight = 1f, fill = false)
                .heightIn(max = TOPIC_LIST_MAX_HEIGHT)
                .padding(horizontal = SoptTheme.spacing.s8),
        ) {
            items(
                items = topics,
                key = { it.topicId },
            ) { topic ->
                SopletterTopicSelectItem(
                    topic = topic,
                    isSelected = topic.topicId == selectedTopicId,
                    onClick = { onTopicClick(topic.topicId) },
                )
            }
        }

        Spacer(modifier = Modifier.height(SoptTheme.spacing.s12))

        MdsActionButton(
            text = "선택 완료",
            enabled = selectedTopicId != null,
            type = MdsActionButtonType.PRIMARY,
            size = MdsActionButtonSize.LARGE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = SoptTheme.spacing.s16,
                    end = SoptTheme.spacing.s16,
                    top = SoptTheme.spacing.s8,
                ),
        ) {
            selectedTopicId?.let(onSelectComplete)
        }
    }
}

@Composable
private fun SopletterTopicSelectItem(
    topic: SopletterTopic,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) SoptTheme.colors.bg.neutral.ghostPressed else Color.Transparent,
                shape = RoundedCornerShape(SoptTheme.radius.r8),
            )
            .noRippleClickable(onClick = onClick)
            .padding(SoptTheme.spacing.s10),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s2),
            modifier = Modifier.weight(1f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s6),
            ) {
                if (topic.isHot) {
                    SopletterTopicTag(text = "HOT🔥", type = MdsTagType.PRIMARY)
                }
                if (topic.isNew) {
                    SopletterTopicTag(text = "NEW!", type = MdsTagType.SECONDARY)
                }

                Text(
                    text = topic.title,
                    style = SoptTheme.typography.label3,
                    color = SoptTheme.colors.fg.neutral.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            topic.subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = SoptTheme.typography.body3,
                    color = SoptTheme.colors.fg.neutral.subtle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        // 선택 여부와 상관없이 아이콘 자리를 잡아 둬서 선택할 때 높이, 폭 고정
        Box(modifier = Modifier.size(24.dp)) {
            if (isSelected) {
                Icon(
                    imageVector = ImageVector.vectorResource(MdsIcons.checkOutlined),
                    contentDescription = null,
                    tint = SoptTheme.colors.bg.secondary.default,
                )
            }
        }
    }
}

@Composable
private fun SopletterTopicTag(
    text: String,
    type: MdsTagType,
) {
    MdsTag(
        text = text,
        type = type,
        emphasis = MdsTagEmphasis.SUBTLE,
        size = MdsTagSize.SMALL,
        shape = MdsTagShape.PILL,
    )
}

@Preview
@Composable
private fun SopletterTopicSelectDialogPreview() {
    SoptTheme {
        SopletterTopicSelectContent(
            topics = persistentListOf(
                SopletterTopic(topicId = 1, title = "전체", subtitle = "자유롭게 수다 나누는 방", isNew = false, isHot = true),
                SopletterTopic(topicId = 2, title = "36기 앱잼 회고", subtitle = "얘들아 앱잼 어땠어?", isNew = true, isHot = false),
                SopletterTopic(topicId = 3, title = "크리스마스 D-100", subtitle = "크리스마스때 모해 얘드라", isNew = false, isHot = false),
            ),
            selectedTopicId = 2,
            onTopicClick = {},
            onBackClick = {},
            onSelectComplete = {},
        )
    }
}
