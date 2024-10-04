package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import okhttp3.internal.immutableListOf
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray30
import org.sopt.official.designsystem.Gray800
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.R.drawable.ic_checkbox_off
import org.sopt.official.feature.fortune.R.drawable.ic_checkbox_on

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokeMessageBottomSheetScreen(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    isAnonymous: Boolean,
    selectedIndex: Int,
    onItemClick: (selectedIndex: Int, message: String) -> Unit,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        containerColor = Gray800,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = null,
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 12.dp)
                .background(color = Gray800),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "함께 보낼 메시지를 선택해주세요",
                    style = SoptTheme.typography.heading20B,
                    color = Gray30,
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                Image(
                    imageVector = if (isAnonymous) ImageVector.vectorResource(id = ic_checkbox_on)
                    else ImageVector.vectorResource(id = ic_checkbox_off),
                    contentDescription = "익명 체크박스",
                    modifier = Modifier.clickable { onIconClick() }
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                Text(
                    text = "익명",
                    style = SoptTheme.typography.title16SB,
                    color = Gray10,
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            LazyColumn(
                contentPadding = PaddingValues(vertical = 4.dp),
            ) {
                val messages = immutableListOf(
                    "안녕하세요? I AM 35기에요",
                    "친해지고 싶어서 DM 드려요 ^^~",
                    "이야기 해보고 싶었어요!!",
                    "모각작 하실래요?",
                    "콕 \uD83D\uDC48",
                )

                itemsIndexed(messages) { index, message ->
                    PokeMessageItem(
                        message = message,
                        isSelected = index == selectedIndex,
                        onItemClick = { onItemClick(index, message) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PokeMessageBottomSheetScreenPreview() {
    SoptTheme {
        PokeMessageBottomSheetScreen(
            sheetState = rememberModalBottomSheetState(
                confirmValueChange = { true },
            ),
            onDismissRequest = { },
            isAnonymous = false,
            selectedIndex = 0,
            onItemClick = { _, _ -> },
            onIconClick = { },
        )
    }
}
