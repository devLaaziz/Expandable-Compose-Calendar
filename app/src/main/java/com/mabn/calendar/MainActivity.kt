package com.mabn.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mabn.calendarlibrary.ExpandableCalendar
import com.mabn.calendar.ui.theme.CalendarTheme
import com.mabn.calendarlibrary.core.calendarDefaultTheme
import java.time.LocalDate
import java.time.Month

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Calendar()
                }
            }
        }
    }
}

@Composable
fun Calendar() {
    val currentDate = remember { mutableStateOf(LocalDate.now()) }
    var currentMonth = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        ExpandableCalendar(
            theme = calendarDefaultTheme.copy(
                dayShape = CircleShape,
                backgroundColor = Color.Black,
                selectedDayBackgroundColor = Color.White,
                dayValueTextColor = Color.White,
                selectedDayValueTextColor = Color.Black,
                headerTextColor = Color.White,
                weekDaysTextColor = Color.White,
                weekDaysTitleTextColor = Color.LightGray,
                weekDaysTextSize = 13.sp,
                headerValueTextSize = 15.sp
            ), onDayClick = {
                currentDate.value = it
            }, daysWithBadge = listOf(LocalDate.now(), LocalDate.of( 2023,  12, 3)),
            onMonthChanged = {
                currentMonth.value = it.value.month.value.toString()
            }
            )
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Text("Selected date: ${currentDate.value}")
            Text("Selected mounth: ${currentMonth.value}")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalendarTheme {
        Calendar()
    }
}