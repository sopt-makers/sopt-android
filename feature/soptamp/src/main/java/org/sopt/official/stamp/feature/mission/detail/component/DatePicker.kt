package org.sopt.official.stamp.feature.mission.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Calendar
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.component.button.SoptampButton
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
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
        .then(
            remember(isEmpty, isEditable) {
                if (isEmpty || !isEditable) {
                    Modifier
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        )

    val backgroundColor = remember(isEmpty, isEditable) {
        if (isEmpty || !isEditable) {
            Gray50
        } else {
            White
        }
    }

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(9.dp))
            .noRippleClickable { if (isEditable) onClicked() }
    ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataPickerBottomSheet(onSelected: (String) -> Unit, onDismissRequest: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    val chosenYear = remember { mutableIntStateOf(currentYear) }
    val chosenMonth = remember { mutableIntStateOf(currentMonth) }
    val chosenDay = remember { mutableIntStateOf(currentDay) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        DatePickerUI(
            currentYear = chosenYear.value,
            currentMonth = chosenMonth.value,
            onYearChosen = { chosenYear.value = it },
            onMonthChosen = { chosenMonth.value = it },
            onDayChosen = { chosenDay.value = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
        SoptampButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = "확인",
            onClicked = { onSelected("${chosenYear.value}.${chosenMonth.value}.${chosenDay.value}") }
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun DatePickerUI(
    currentYear: Int,
    currentMonth: Int,
    onYearChosen: (Int) -> Unit,
    onMonthChosen: (Int) -> Unit,
    onDayChosen: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 5.dp)
    ) {
        DateSelectionSection(
            currentYear = currentYear,
            currentMonth = currentMonth,
            onYearChosen = { onYearChosen(it.toInt()) },
            onMonthChosen = { onMonthChosen(it.toInt()) },
            onDayChosen = { onDayChosen(it.toInt()) },
        )
    }
}

@Composable
fun DateSelectionSection(
    currentYear: Int,
    currentMonth: Int,
    onYearChosen: (String) -> Unit,
    onMonthChosen: (String) -> Unit,
    onDayChosen: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        InfiniteItemsPicker(
            items = years,
            firstIndex = Int.MAX_VALUE / 2 + (currentYear - 1967),
            onItemSelected = onYearChosen
        )
        Spacer(modifier = Modifier.width(10.dp))
        InfiniteItemsPicker(
            items = monthsNumber,
            firstIndex = Int.MAX_VALUE / 2 - 4 + currentMonth,
            onItemSelected = onMonthChosen
        )
        Spacer(modifier = Modifier.width(10.dp))
        InfiniteItemsPicker(
            items = when {
                (currentYear % 4 == 0) && currentMonth == 2 -> febDaysLeap
                currentMonth == 2 -> febDays
                else -> days
            },
            firstIndex = Int.MAX_VALUE / 2 + (currentDay - 2),
            onItemSelected = onDayChosen
        )
    }
}

@Composable
fun InfiniteItemsPicker(modifier: Modifier = Modifier, items: List<String>, firstIndex: Int, onItemSelected: (String) -> Unit,) {
    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
        onItemSelected(currentValue.value)
        listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
    }

    Box(
        modifier = Modifier.height(108.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            HorizontalDivider(modifier = Modifier.size(height = 1.dp, width = 60.dp), color = SoptTheme.colors.black)
            HorizontalDivider(modifier = Modifier.size(height = 1.dp, width = 60.dp), color = SoptTheme.colors.black)
        }
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
        ) {
            items(count = Int.MAX_VALUE) {
                val index = it % items.size
                if (it == listState.firstVisibleItemIndex + 1) {
                    currentValue.value = items[index]
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = items[index],
                    modifier = Modifier.alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f),
                    style = SoptTheme.typography.h1,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

val currentYear = Calendar.getInstance().get(Calendar.YEAR)
val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

val years = (1950..2050).map { it.toString() }
val monthsNumber = (1..12).map { it.toString() }
val days = (1..31).map { it.toString() }
val febDays = (1..28).map { it.toString() }
val febDaysLeap = (1..29).map { it.toString() }
val monthsNames = listOf(
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec"
)

@DefaultPreview
@Composable
private fun DatePickerPreview() {
    SoptTheme {
        DatePicker(
            value = "2024.12.25",
            onClicked = {},
            borderColor = getLevelTextColor(2),
            placeHolder = "날짜를 입력해주세요.",
            isEditable = true
        )
    }
}

@DefaultPreview
@Composable
private fun CustomDatePickerPreview() {
    val chosenYear = remember { mutableStateOf(currentYear) }
    val chosenMonth = remember { mutableStateOf(currentMonth) }
    val chosenDay = remember { mutableStateOf(currentDay) }
    SoptTheme {
        DatePickerUI(
            currentYear = chosenYear.value,
            currentMonth = chosenMonth.value,
            onYearChosen = { chosenYear.value = it },
            onMonthChosen = { chosenMonth.value = it },
            onDayChosen = { chosenDay.value = it }
        )
    }
}
