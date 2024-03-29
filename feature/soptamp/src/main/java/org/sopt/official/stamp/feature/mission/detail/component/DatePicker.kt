package org.sopt.official.stamp.feature.mission.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.Gray50
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.designsystem.style.White
import org.sopt.official.stamp.feature.ranking.getLevelTextColor
import org.sopt.official.stamp.util.DefaultPreview

@Composable
fun DatePicker(value: String, placeHolder: String, onClicked: () -> Unit, borderColor: Color, isEditable: Boolean) {
    val isEmpty = remember(value) { value.isEmpty() }

    val modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 39.dp)
        .clip(RoundedCornerShape(9.dp))

    val modifierWithBorder = remember(isEmpty, isEditable) {
        if (isEmpty || !isEditable) {
            modifier
        } else {
            modifier.border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
        }
    }

    val backgroundColor = remember(isEmpty, isEditable) {
        if (isEmpty || !isEditable) {
            Gray50
        } else {
            White
        }
    }

    Box(modifier = modifierWithBorder
        .background(backgroundColor, RoundedCornerShape(9.dp))
        .clickable { if (isEditable) onClicked() })
    {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(),
                text = if (isEmpty) placeHolder else value,
                color = if (isEmpty) SoptTheme.colors.onSurface60 else SoptTheme.colors.onSurface90,
                style = SoptTheme.typography.caption1

            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.right_forward),
                contentDescription = "date picker icon",
                tint = SoptTheme.colors.onSurface60
            )
        }
    }
}

@DefaultPreview
@Composable
private fun MemoPreview() {
    SoptTheme {
        DatePicker(
            value = "",
            onClicked = {},
            borderColor = getLevelTextColor(2),
            placeHolder = "날짜를 입력해주세요.",
            isEditable = true
        )
    }
}
