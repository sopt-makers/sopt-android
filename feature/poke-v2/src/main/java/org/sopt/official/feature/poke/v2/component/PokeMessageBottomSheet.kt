package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.Gray30
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.mds.theme.SoptTheme
import org.sopt.official.designsystem.SoptTheme as LegacySoptTheme

/**
 * 콕찌르기 메시지 선택 바텀시트
 *
 * @param isAnonymous                현재 익명 여부
 * @param isAnonymousCheckboxLocked  true 이면 익명 불가 (체크박스 미체크 고정)
 * @param onAnonymousCheckboxClick   체크박스 탭 시 호출
 * @param onMessageClick             선택한 메시지 전달
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokeMessageBottomSheet(
    messages: ImmutableList<PokeMessageUiState>,
    isAnonymous: Boolean,
    isAnonymousCheckboxLocked: Boolean,
    onMessageClick: (PokeMessageUiState) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "보낼 메시지를 골라주세요",
    onAnonymousCheckboxClick: () -> Unit = {},
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SoptTheme.colors.bg.neutral.ghost,
        shape = RoundedCornerShape(
            topStart = SoptTheme.radius.r20,
            topEnd = SoptTheme.radius.r20,
        ),
        dragHandle = null,
        modifier = modifier,
    ) {
        PokeMessageBottomSheetContent(
            title = title,
            messages = messages,
            isAnonymous = isAnonymous,
            isAnonymousCheckboxLocked = isAnonymousCheckboxLocked,
            onAnonymousClick = onAnonymousCheckboxClick,
            onMessageClick = onMessageClick,
        )
    }
}

@Composable
internal fun PokeMessageBottomSheetContent(
    title: String,
    messages: ImmutableList<PokeMessageUiState>,
    isAnonymous: Boolean,
    isAnonymousCheckboxLocked: Boolean,
    onAnonymousClick: () -> Unit,
    onMessageClick: (PokeMessageUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s12),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = SoptTheme.spacing.s20,
                end = SoptTheme.spacing.s20,
                top = SoptTheme.spacing.s24,
                bottom = SoptTheme.spacing.s32,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = title,
                style = SoptTheme.typography.title3,
                color = SoptTheme.colors.fg.neutral.bold,
                modifier = Modifier.weight(1f),
            )

            PokeAnonymousCheckbox(
                checked = isAnonymous && !isAnonymousCheckboxLocked,
                onClick = onAnonymousClick,
            )
        }

        messages.fastForEach { message ->
            PokeMessageItem(
                message = message,
                onClick = { onMessageClick(message) },
            )
        }
    }
}

/**
 * 익명 체크박스
 * - TODO Figma mds 체크박스 스펙 확정 후 MdsCheckbox 로 교체 필요
 */
@Composable
private fun PokeAnonymousCheckbox(
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.noRippleClickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(
                if (checked) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked,
            ),
            contentDescription = null,
            modifier = Modifier.size(26.dp),
        )
        Text(
            text = "익명",
            style = LegacySoptTheme.typography.heading18B.copy(lineHeight = 20.sp),
            color = Gray30,
        )
    }
}

@Composable
private fun PokeMessageItem(
    message: PokeMessageUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if (isPressed) SoptTheme.colors.bg.neutral.ghostHover else Color.Transparent

    Text(
        text = message.content,
        style = SoptTheme.typography.label2,
        color = SoptTheme.colors.fg.neutral.bold,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SoptTheme.radius.r6))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = SoptTheme.spacing.s8, vertical = SoptTheme.spacing.s12),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF202025)
@Composable
private fun PokeMessageBottomSheetContentPreview() {
    SoptTheme {
        LegacySoptTheme {
            PokeMessageBottomSheetContent(
                title = "보낼 메시지를 골라주세요",
                messages = persistentListOf(
                    PokeMessageUiState(messageId = 1, content = "메시지 내용을 넣어봐요"),
                    PokeMessageUiState(messageId = 2, content = "메시지 내용을 넣어봐요"),
                    PokeMessageUiState(messageId = 3, content = "메시지 내용을 넣어봐요"),
                    PokeMessageUiState(messageId = 4, content = "메시지 내용을 넣어봐요"),
                    PokeMessageUiState(messageId = 5, content = "메시지 내용을 넣어봐요"),
                ),
                isAnonymous = true,
                isAnonymousCheckboxLocked = false,
                onAnonymousClick = {},
                onMessageClick = {},
            )
        }
    }
}
