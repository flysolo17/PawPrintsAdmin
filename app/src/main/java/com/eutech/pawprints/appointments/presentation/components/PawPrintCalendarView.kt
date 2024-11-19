package com.eutech.pawprints.appointments.presentation.components
import android.icu.util.LocaleData
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eutech.pawprints.appointments.data.appointment.Appointments

import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.data.countSlots
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import java.util.Date
import java.util.Locale

@Composable
fun PawPrintCalendarView(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    schedules : List<Schedule>,
    appointments : List<Appointments>,
    selectedDate : LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange : (LocalDate) -> Unit,
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {
                currentMonth = currentMonth.minusMonths(1)
                val date = LocalDate.of(currentMonth.year, currentMonth.month, 1)
                onMonthChange(date)

            }

            ) {
                Text("<", fontSize = 24.sp)
            }
            val display = if (isLoading)"Getting schedule and appointments" else currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " ${selectedDate.dayOfMonth}, " + currentMonth.year
            Text(
                text = display,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            TextButton(onClick = {
                currentMonth = currentMonth.plusMonths(1)
                val date = LocalDate.of(currentMonth.year, currentMonth.month, 1)
                onMonthChange(date)
            }) {
                Text(">", fontSize = 24.sp)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
        val daysInMonth = getDaysInMonthGrid(currentMonth)
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(daysInMonth.size) { index ->
                val date = daysInMonth[index]
                val isSelected = date == selectedDate
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clickable {
                            onDateSelected(date!!)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = date?.dayOfMonth?.toString() ?: "",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color =     if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    if (date != null && date.month == currentMonth.month) MaterialTheme.colorScheme.onSurface else Color.Gray
                                },
                                fontWeight = if (isSelected) {
                                    FontWeight.Black
                                } else FontWeight.Normal
                            ),
                            textAlign = TextAlign.Center
                        )
                        Box(
                            modifier = modifier.size(8.dp)
                                .background(
                                    color = if(isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                        )

                    }
                    date?.let {
                        if (it.dayOfWeek.value != 6 && it.dayOfWeek.value != 7) {
                            val slots = schedules.countSlots(it)
                            Text(
                                text = if (slots == 0) "No Slots" else "$slots slots",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (slots> 0) MaterialTheme.colorScheme.primary else Color.Gray,
                                    fontWeight = if (slots > 0) FontWeight.Bold else FontWeight.Normal
                                ),
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getDaysInMonthGrid(yearMonth: YearMonth): List<LocalDate?> {
    val daysInMonth = mutableListOf<LocalDate?>()
    val firstDayOfMonth = yearMonth.atDay(1)
    val dayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInPreviousMonth = dayOfWeek
    val totalDays = yearMonth.lengthOfMonth()

    for (i in 0 until daysInPreviousMonth) {
        daysInMonth.add(null)
    }

    for (day in 1..totalDays) {
        daysInMonth.add(yearMonth.atDay(day))
    }

    return daysInMonth
}
