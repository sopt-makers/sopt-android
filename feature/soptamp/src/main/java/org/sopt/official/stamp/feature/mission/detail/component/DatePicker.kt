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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import okhttp3.internal.immutableListOf
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.component.button.SoptampButton
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.Gray50
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.designsystem.style.White
import org.sopt.official.stamp.feature.ranking.getLevelTextColor
import org.sopt.official.stamp.util.DefaultPreview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DatePicker(
    value: String,
    placeHolder: String,
    borderColor: Color,
    isEditable: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isEmpty = remember(value) { value.isEmpty() }

    val newModifier = modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 39.dp)
        .clip(RoundedCornerShape(9.dp))
        .then(
            if (isEmpty || !isEditable) {
                Modifier
            } else {
                Modifier.border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(10.dp)
                )
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
        modifier = newModifier
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

    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
    var chosenYear by remember { mutableIntStateOf(currentYear) }
    var chosenMonth by remember { mutableIntStateOf(currentMonth) }
    var chosenDay by remember { mutableIntStateOf(currentDay) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        DatePickerUI(
            currentYear = chosenYear,
            currentMonth = chosenMonth,
            currentDay = chosenDay,
            onYearChosen = { chosenYear = it },
            onMonthChosen = { chosenMonth = it },
            onDayChosen = { chosenDay = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
        SoptampButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = "확인",
            onClicked = {
                val calendar = Calendar.getInstance().apply {
                    set(chosenYear, chosenMonth - 1, chosenDay)
                }
                val formattedDate = formatter.format(calendar.time)

                onSelected(formattedDate)
            }
        )
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun DatePickerUI(
    currentYear: Int,
    currentMonth: Int,
    currentDay: Int,
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
            currentDay = currentDay,
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
    currentDay: Int,
    onYearChosen: (String) -> Unit,
    onMonthChosen: (String) -> Unit,
    onDayChosen: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        DateItemsPicker(
            items = years.toImmutableList(),
            firstIndex = (currentYear - startYear),
            onItemSelected = onYearChosen
        )
        Spacer(modifier = Modifier.width(10.dp))
        DateItemsPicker(
            items = monthsNumber.toImmutableList(),
            firstIndex = currentMonth,
            onItemSelected = onMonthChosen
        )
        Spacer(modifier = Modifier.width(10.dp))
        DateItemsPicker(
            items = when {
                (currentYear % 4 == 0) && currentMonth == 2 -> days29.toImmutableList()
                currentMonth == 2 -> days28.toImmutableList()
                days30Months.contains(currentMonth) -> days30.toImmutableList()
                else -> days31.toImmutableList()
            },
            firstIndex = currentDay - 1,
            onItemSelected = onDayChosen
        )
    }
}

@Composable
fun DateItemsPicker(
    items: ImmutableList<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(!listState.isScrollInProgress) {
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
            HorizontalDivider(
                modifier = Modifier.size(height = 1.dp, width = 60.dp),
                color = SoptTheme.colors.black
            )
            HorizontalDivider(
                modifier = Modifier.size(height = 1.dp, width = 60.dp),
                color = SoptTheme.colors.black
            )
        }
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
        ) {
            items(items.size) {
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
val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

const val startYear = 1950
const val endYear = 2100
val years = (listOf("") + (startYear..endYear).map { it.toString() } + listOf("")).toImmutableList()
val monthsNumber = (listOf("") + (1..12).map { it.toString() } + listOf("")).toImmutableList()
val days28 = (listOf("") + (1..28).map { it.toString() } + listOf("")).toImmutableList()
val days29 = (listOf("") + (1..29).map { it.toString() } + listOf("")).toImmutableList()
val days30 = (listOf("") + (1..30).map { it.toString() } + listOf("")).toImmutableList() // 4,6,9,11
val days31 = (listOf("") + (1..31).map { it.toString() } + listOf("")).toImmutableList() // 1,3,5,7,8,10.12
val days30Months = immutableListOf(4, 6, 9, 11)

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
            currentDay = chosenDay.value,
            onYearChosen = { chosenYear.value = it },
            onMonthChosen = { chosenMonth.value = it },
            onDayChosen = { chosenDay.value = it }
        )
    }
}
