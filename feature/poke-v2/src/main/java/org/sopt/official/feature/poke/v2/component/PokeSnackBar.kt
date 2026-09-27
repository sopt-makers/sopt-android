package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.mds.theme.SoptTheme

/**
 * 콕찌르기 스낵바
 * TODO mds 스낵바 추가 시 교체 필요
 */
enum class PokeSnackBarType {
    SUCCESS,
    WARNING,
    FAILURE,
}

class PokeSnackBarVisuals(
    override val message: String,
    val type: PokeSnackBarType = PokeSnackBarType.WARNING,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals

suspend fun SnackbarHostState.showPokeSnackBar(
    message: String,
    type: PokeSnackBarType = PokeSnackBarType.WARNING
): SnackbarResult = showSnackbar(PokeSnackBarVisuals(message = message, type = type))

@Composable
internal fun PokeSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier
    ) { data ->
        when (val visuals = data.visuals) {
            is PokeSnackBarVisuals -> PokeSnackBar(message = visuals.message, type = visuals.type)
            // showSnackbar(message) 로 직접 호출된 경우. showPokeSnackBar 사용을 권장
            else -> PokeSnackBar(message = visuals.message, type = PokeSnackBarType.WARNING)
        }
    }
}

@Composable
internal fun PokeSnackBar(
    message: String,
    modifier: Modifier = Modifier,
    type: PokeSnackBarType = PokeSnackBarType.WARNING
) {
    val iconRes = when (type) {
        PokeSnackBarType.SUCCESS -> R.drawable.icon_success
        PokeSnackBarType.WARNING -> R.drawable.icon_warning
        PokeSnackBarType.FAILURE -> R.drawable.icon_failure
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s8),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.bg.neutral.inverse,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(SoptTheme.spacing.s16)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = message,
            style = SoptTheme.typography.body2,
            color = SoptTheme.colors.fg.neutral.inverse,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1012)
@Composable
private fun PokeSnackBarPreview() {
    SoptTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PokeSnackBar(message = "콕 찌르기를 완료했어요.", type = PokeSnackBarType.SUCCESS)
            PokeSnackBar(message = "익명 해제 시, 상대방이 나를 알 수 있어요.", type = PokeSnackBarType.WARNING)
            PokeSnackBar(message = "문제가 발생했습니다.", type = PokeSnackBarType.FAILURE)
        }
    }
}
