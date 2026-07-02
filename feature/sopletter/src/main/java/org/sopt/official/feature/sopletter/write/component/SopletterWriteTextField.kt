package org.sopt.official.feature.sopletter.write.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun SopletterWriteTextBox(
    userName: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    maxLength: Int = 350,
    onLimitExceeded: () -> Unit = {}
) {
    val isAtLimit = state.text.length >= maxLength
    val borderColor = if (isAtLimit) SoptTheme.colors.error else Color.Transparent
    val counterColor = if (isAtLimit) SoptTheme.colors.error else SoptTheme.colors.onSurface300

    val scrollState = rememberScrollState()

    val inputTransformation = remember(maxLength) {
        InputTransformation {
            if (length > maxLength) {
                revertAllChanges()
                onLimitExceeded()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = SoptTheme.colors.onSurface700, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = userName,
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.onSurface10
        )

        BasicTextField(
            state = state,
            scrollState = scrollState,
            textStyle = TextStyle(
                color = SoptTheme.colors.onSurface10,
                fontSize = 14.sp,
                lineHeight = 22.sp
            ),
            cursorBrush = SolidColor(SoptTheme.colors.onSurface10),
            lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 5, maxHeightInLines = 10),
            inputTransformation = inputTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScrollbar(
                    scrollState = scrollState,
                    color = SoptTheme.colors.onSurface300.copy(alpha = 0.5f)
                ),
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = "나와 같은 기수의 솝트인들에게 전하고 싶은\n말을 자유롭게 적어보세요.",
                            style = SoptTheme.typography.body16R,
                            color = SoptTheme.colors.onSurface400
                        )
                    }
                    innerTextField()
                }
            }
        )

        Text(
            text = "${state.text.length}/${maxLength}자",
            style = SoptTheme.typography.body14M,
            color = counterColor,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

//TODO : main 쪽 scrollbar 사용하는거 고려
private fun Modifier.verticalScrollbar(
    scrollState: ScrollState,
    color: Color,
    width: Dp = 4.dp
): Modifier = drawWithContent {
    drawContent()

    val maxValue = scrollState.maxValue.toFloat()
    if (maxValue > 0f) {
        val viewHeight = size.height
        val contentHeight = viewHeight + maxValue
        val thumbHeight = (viewHeight / contentHeight) * viewHeight
        val thumbY = (scrollState.value.toFloat() / maxValue) * (viewHeight - thumbHeight)

        drawRoundRect(
            color = color,
            topLeft = Offset(size.width - width.toPx(), thumbY),
            size = Size(width.toPx(), thumbHeight),
            cornerRadius = CornerRadius(width.toPx() / 2, width.toPx() / 2)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SopletterWriteTextBoxPreview() {
    val state = rememberTextFieldState()
    SoptTheme {
        SopletterWriteTextBox(
            userName = "익명의 무무",
            state = state
        )
    }
}