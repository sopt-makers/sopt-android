package org.sopt.official.stamp.feature.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RankingBar(width: Dp, height: Dp, rank: Int, content: @Composable () -> Unit = {}) {
    Box(
        modifier = Modifier
            .size(width, height)
            .background(
                color = getRankBackgroundColor(rank),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        content()
    }
}
