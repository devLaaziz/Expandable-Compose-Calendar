package com.mabn.calendarlibrary

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mabn.calendarlibrary.component.InlineCalendar
import com.mabn.calendarlibrary.component.MonthText
import com.mabn.calendarlibrary.component.MonthViewCalendar
import com.mabn.calendarlibrary.component.ToggleExpandCalendarButton
import com.mabn.calendarlibrary.core.CalendarIntent
import com.mabn.calendarlibrary.core.CalendarTheme
import com.mabn.calendarlibrary.core.Period
import com.mabn.calendarlibrary.core.calendarDefaultTheme
import com.mabn.calendarlibrary.utils.getWeekStartDate
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ExpandableCalendar(
    onDayClick: (LocalDate) -> Unit,
    theme: CalendarTheme = calendarDefaultTheme,
    daysWithBadge: List<LocalDate>,
    onMonthChanged: (month: State<YearMonth>) -> Unit
) {
    val viewModel: CalendarViewModel = viewModel()
    val loadedDates = viewModel.visibleDates.collectAsState()
    val selectedDate = viewModel.selectedDate.collectAsState()
    val calendarExpanded = viewModel.calendarExpanded.collectAsState()
    val currentMonth = viewModel.currentMonth.collectAsState()
    onMonthChanged(viewModel.currentMonth.collectAsState())
    ExpandableCalendar(
        loadedDates = loadedDates.value,
        selectedDate = selectedDate.value,
        currentMonth = currentMonth.value,
        onIntent = viewModel::onIntent,
        calendarExpanded = calendarExpanded.value,
        theme = theme,
        onDayClick = onDayClick,
        daysWithBadge = daysWithBadge,
    )
}

@Composable
private fun ExpandableCalendar(
    loadedDates: Array<List<LocalDate>>,
    selectedDate: LocalDate,
    currentMonth: YearMonth,
    onIntent: (CalendarIntent) -> Unit,
    calendarExpanded: Boolean,
    theme: CalendarTheme,
    onDayClick: (LocalDate) -> Unit,
    daysWithBadge: List<LocalDate>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .animateContentSize()
            .background(theme.backgroundColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 10.dp, start = 15.dp)
                .fillMaxWidth()
                .background(theme.headerBackgroundColor)
        ) {
            MonthText(selectedMonth = currentMonth, theme = theme)
            Spacer(Modifier.weight(1f))
            ToggleExpandCalendarButton(
                isExpanded = calendarExpanded,
                expand = { onIntent(CalendarIntent.ExpandCalendar) },
                collapse = { onIntent(CalendarIntent.CollapseCalendar) },
                color = theme.headerTextColor
            )
        }
        if (calendarExpanded) {
            MonthViewCalendar(
                loadedDates,
                selectedDate,
                daysWithBadge,
                theme = theme,
                currentMonth = currentMonth,
                loadDatesForMonth = { yearMonth ->
                    onIntent(
                        CalendarIntent.LoadNextDates(
                            yearMonth.atDay(
                                1
                            ), period = Period.MONTH
                        )
                    )
                },
                onDayClick = {
                    onIntent(CalendarIntent.SelectDate(it))
                    onDayClick(it)
                }
            )
        } else {
            InlineCalendar(
                loadedDates,
                selectedDate,
                daysWithBadge,
                theme = theme,
                loadNextWeek = { nextWeekDate -> onIntent(CalendarIntent.LoadNextDates(nextWeekDate)) },
                loadPrevWeek = { endWeekDate ->
                    onIntent(
                        CalendarIntent.LoadNextDates(
                            endWeekDate.minusDays(1).getWeekStartDate()
                        )
                    )
                },
                onDayClick = {
                    onIntent(CalendarIntent.SelectDate(it))
                    onDayClick(it)
                }
            )
        }
    }
}








