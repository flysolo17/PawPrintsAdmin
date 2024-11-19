package com.eutech.pawprints.appointments.data.appointment


import com.eutech.pawprints.schedule.data.Hours
import com.eutech.pawprints.schedule.data.display
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Appointments(
    val id : String  ? = null,
    val userID : String ? = null,
    val title : String ? = null,
    val note : String ? = null,
    val attendees : List<Attendees> = emptyList(),
    val pets : List<String> = emptyList(),
    val scheduleDate : String ? =null,
    val startTime : Hours? = null,
    val endTime : Hours? = null,
    val status: AppointmentStatus? = AppointmentStatus.PENDING,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
)


data class Attendees(
    val id : String ? = null,
    val name : String ? = null,
    val phone : String ? = null,
    val email : String ? = null,
    val type : AttendeeType? = AttendeeType.CLIENT
)

enum class AttendeeType {
    CLIENT,
    DOCTOR
}


fun Appointments.displayTime() : String {
    return "${this.startTime?.display()} - ${this.endTime?.display()}"
}

fun Appointments.getFormattedDate(): Date? {
    val formatter = SimpleDateFormat("MMM, dd yyyy", Locale.getDefault())
    return try {
        this.scheduleDate?.let { formatter.parse(it) } // Parses the date string into a Date object
    } catch (e: Exception) {
        null // Returns null if the date cannot be parsed
    }
}
