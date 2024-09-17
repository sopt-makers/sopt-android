package org.sopt.official.feature.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray400
import org.sopt.official.designsystem.Gray900
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.model.MyPageUiModel

@Composable
fun MyPageSection(items: List<MyPageUiModel>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .background(
                color = Gray900,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        items.forEach { item ->
            when (item) {
                is MyPageUiModel.Header -> {
                    Text(
                        text = item.title,
                        style = SoptTheme.typography.label12SB,
                        color = Gray400,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(23.dp))
                }

                is MyPageUiModel.MyPageItem -> {
                    MyPageItem(
                        text = item.title,
                        onButtonClick = item.onItemClick
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}