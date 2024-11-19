package com.eutech.pawprints.schedule.presentation

import androidx.core.app.NotificationCompat.StreamType
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.data.ScheduleWithDoctor


data class ScheduleState(
    val isLoading : Boolean = false,
    val schedules : List<Schedule> = emptyList(),
    val doctors : List<Doctors> = emptyList(),
    val errors : String? = null,
    val isCreatingSchedule : Boolean = false,
    val created : String ? = null,
    val isDeleting : String ? = null,
    val deleted : String ? = null,
)