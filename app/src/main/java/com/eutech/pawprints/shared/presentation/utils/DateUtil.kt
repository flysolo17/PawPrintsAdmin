package com.eutech.pawprints.shared.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.chargemap.compose.numberpicker.Hours
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

object DateUtil {

    val daysOfWeek: Array<String>
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val daysOfWeek = Array(7) { "" }

            for (dayOfWeek in DayOfWeek.entries) {
                val localizedDayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                daysOfWeek[dayOfWeek.value - 1] = localizedDayName
            }

            return daysOfWeek
        }
}

@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.getDayOfMonthStartingFromMonday(): List<LocalDate> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val firstMondayOfMonth = firstDayOfMonth.with(DayOfWeek.MONDAY)
    val firstDayOfNextMonth = firstDayOfMonth.plusMonths(1)

    return generateSequence(firstMondayOfMonth) { it.plusDays(1) }
        .takeWhile { it.isBefore(firstDayOfNextMonth) }
        .toList()
}

@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.getDisplayName(): String {
    return "${month.getDisplayName(TextStyle.FULL, Locale.getDefault())} $year"
}

@RequiresApi(Build.VERSION_CODES.O)
fun Hours.toLocalTime(): LocalTime {
    val hour = this.hours
    val minute = this.minutes
    return LocalTime.of(hour, minute)
}


fun Date.displayDate(): String {
    val formatter = SimpleDateFormat("MMM, dd yyyy hh:mm aa", Locale.getDefault())
    return formatter.format(this)
}