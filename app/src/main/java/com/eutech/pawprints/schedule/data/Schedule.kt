package com.eutech.pawprints.schedule.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import org.threeten.bp.LocalDate
import java.sql.Time
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale

import org.threeten.bp.format.DateTimeFormatter


fun LocalDate.getStartDayOfMonth(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM, dd yyyy")
    return this.withDayOfMonth(1).format(formatter)
}

fun LocalDate.getEndDayOfMonth(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM, dd yyyy")
    return this.withDayOfMonth(this.lengthOfMonth()).format(formatter)
}


fun List<Schedule>.countSlots(localDate: LocalDate): Int {
    val formatter = SimpleDateFormat("MMM, dd yyyy", Locale.getDefault())
    val targetDate = formatter.format(java.sql.Date.valueOf(localDate.toString()))

    return this
        .filter { schedule ->
            schedule.date?.let {
                it == targetDate
            } ?: false
        }
        .sumOf { it.slots ?: 0 }
}

data class Schedule(
    val id : String ? = null,
    val doctorID : String ? = null,
    val date : String? = null,
    val startTime   : Hours? = null,
    val endTime : Hours? = null,
    val slots : Int ? = null,
)

data class Hours(
    val hour : Int ? = null,
    val minute : Int ? = null,
    val meridiem : Meridiem? = null
)


enum class Meridiem {
    AM,
    PM
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toPawPrintTime(): Hours {
    // Determine if it's AM or PM
    val meridiem = if (this.hour >= 12) {
        Meridiem.PM
    } else {
        Meridiem.AM
    }
    val hourIn12Format = when {
        this.hour == 0 -> 12  // Midnight case
        this.hour > 12 -> this.hour - 12
        else -> this.hour
    }


    return Hours(
        hour = hourIn12Format,
        minute = this.minute,
        meridiem = meridiem
    )
}


fun Hours.display(): String {
    val formattedHour = String.format("%02d", this.hour)
    val formattedMinute = String.format("%02d", this.minute)
    val meridiemString = this.meridiem?.name ?: ""
    return "$formattedHour : $formattedMinute $meridiemString"
}
