/*
 * MIT License
 * Copyright 2024-2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.component.AppjamtampButton

@Composable
internal fun DatePicker(
    value: String,
    placeHolder: String,
    isEditable: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEmpty by remember(value) { derivedStateOf { value.isEmpty() } }

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
                    color = SoptTheme.colors.onSurface600,
                    shape = RoundedCornerShape(9.dp),
                )
            },
        )

    Box(
        modifier = newModifier
            .background(
                SoptTheme.colors.onSurface900,
                RoundedCornerShape(9.dp),
            )
            .clickable { if (isEditable) onClicked() },
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEmpty) placeHolder else value,
                color = if (isEmpty) SoptTheme.colors.onSurface300 else SoptTheme.colors.onSurface50,
                style = SoptTheme.typography.label14SB
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right_32),
                contentDescription = null,
                tint = SoptTheme.colors.onSurface300,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DataPickerBottomSheet(
    onSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
    var chosenYear by remember { mutableIntStateOf(currentYear) }
    var chosenMonth by remember { mutableIntStateOf(currentMonth + 1) }
    var chosenDay by remember { mutableIntStateOf(currentDay) }

    val isValidDate by remember { derivedStateOf { calculateValidDate(chosenYear, chosenMonth, chosenDay) } }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SoptTheme.colors.onSurface800,
    ) {
        DatePickerUI(
            isValidDate = isValidDate,
            chosenYear = chosenYear,
            chosenMonth = chosenMonth,
            chosenDay = chosenDay,
            onYearChosen = { chosenYear = it },
            onMonthChosen = { chosenMonth = it },
            onDayChosen = { chosenDay = it },
        )
        Spacer(modifier = Modifier.height(20.dp))
        AppjamtampButton(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            text = "확인",
            onClicked = {
                if (isValidDate) {
                    val calendar =
                        Calendar.getInstance().apply {
                            set(chosenYear, chosenMonth - 1, chosenDay)
                        }
                    val formattedDate = formatter.format(calendar.time)

                    onSelected(formattedDate)
                }
            },
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

private fun calculateValidDate(
    year: Int,
    month: Int,
    day: Int,
): Boolean {
    val givenCalendar =
        Calendar.getInstance().apply {
            set(year, month - 1, day)
        }
    val referenceCalendar = Calendar.getInstance()

    return !givenCalendar.after(referenceCalendar)
}

@Composable
private fun DatePickerUI(
    isValidDate: Boolean,
    chosenYear: Int,
    chosenMonth: Int,
    chosenDay: Int,
    onYearChosen: (Int) -> Unit,
    onMonthChosen: (Int) -> Unit,
    onDayChosen: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
    ) {
        DateSelectionSection(
            isValidDate = isValidDate,
            chosenYear = chosenYear,
            chosenMonth = chosenMonth,
            chosenDay = chosenDay,
            onYearChosen = { onYearChosen(it.toInt()) },
            onMonthChosen = { onMonthChosen(it.toInt()) },
            onDayChosen = { onDayChosen(it.toInt()) },
        )
    }
}

@Composable
private fun DateSelectionSection(
    isValidDate: Boolean,
    chosenYear: Int,
    chosenMonth: Int,
    chosenDay: Int,
    onYearChosen: (String) -> Unit,
    onMonthChosen: (String) -> Unit,
    onDayChosen: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        DateItemsPicker(
            isValidDate = isValidDate,
            max = YEAR_INDEX,
            items = years,
            firstIndex = (chosenYear - START_YEAR),
            onItemSelected = onYearChosen,
        )
        Spacer(modifier = Modifier.width(10.dp))
        DateItemsPicker(
            isValidDate = isValidDate,
            max = currentMonth,
            items = monthsNumber,
            firstIndex = chosenMonth,
            onItemSelected = onMonthChosen,
        )
        Spacer(modifier = Modifier.width(10.dp))
        DateItemsPicker(
            isValidDate = isValidDate,
            max = currentDay - 1,
            items =
                when {
                    (chosenYear % 4 == 0) && chosenMonth == 2 -> days29
                    chosenMonth == 2 -> days28
                    days30Months.contains(chosenMonth) -> days30
                    else -> days31
                },
            firstIndex = chosenDay - 1,
            onItemSelected = onDayChosen,
        )
    }
}

@Composable
private fun DateItemsPicker(
    isValidDate: Boolean,
    max: Int,
    items: ImmutableList<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(!listState.isScrollInProgress, isValidDate) {
        if (isValidDate) {
            if (currentValue.value.isNotEmpty()) {
                onItemSelected(currentValue.value)
                listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
            }
        } else { // 오늘 이후의 날짜 선택시
            if (currentValue.value.isNotEmpty() && max < currentValue.value.toInt()) {
                listState.animateScrollToItem(index = max)
                onItemSelected(currentValue.value)
            }
        }
    }

    Box(
        modifier = modifier.height(108.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            HorizontalDivider(
                modifier = Modifier.size(height = 1.dp, width = 60.dp),
                color = SoptTheme.colors.onSurface700,
            )
            HorizontalDivider(
                modifier = Modifier.size(height = 1.dp, width = 60.dp),
                color = SoptTheme.colors.onSurface700,
            )
        }
        LazyColumn(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
        ) {
            items(items.size) {
                val index = it % items.size
                val firstVisibleItemIndex by remember {
                    derivedStateOf { listState.firstVisibleItemIndex }
                }
                if (it == firstVisibleItemIndex + 1) {
                    currentValue.value = items[index]
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = items[index],
                    color = if (it == firstVisibleItemIndex + 1) SoptTheme.colors.onSurface10 else SoptTheme.colors.onSurface400,
                    modifier = Modifier.alpha(if (it == firstVisibleItemIndex + 1) 1f else 0.3f),
                    style = SoptTheme.typography.heading20B,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
private val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
private val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

private const val YEAR_INDEX = 50 // 현재 년도의 +- 50년까지 Date Picker에 표시
private val START_YEAR = currentYear - YEAR_INDEX
private val END_YEAR = currentYear + YEAR_INDEX
private val years = (listOf("") + (START_YEAR..END_YEAR).map { it.toString() } + listOf("")).toImmutableList()
private val monthsNumber = (listOf("") + (1..12).map { it.toString() } + listOf("")).toImmutableList()
private val days28 = (listOf("") + (1..28).map { it.toString() } + listOf("")).toImmutableList() // 해당 월 : 2
private val days29 = (listOf("") + (1..29).map { it.toString() } + listOf("")).toImmutableList() // 해당 월 : 윤년 2월
private val days30 = (listOf("") + (1..30).map { it.toString() } + listOf("")).toImmutableList() // 해당 월 : 4,6,9,11
private val days31 = (listOf("") + (1..31).map { it.toString() } + listOf("")).toImmutableList() // 해당 월 : 1,3,5,7,8,10.12
private val days30Months = persistentListOf(4, 6, 9, 11)
