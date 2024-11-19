package com.eutech.pawprints.doctors.data

import com.eutech.pawprints.schedule.data.Schedule

data class DoctorWithSchedules(
    val doctors: Doctors ? = null,
    val schedules : List<Schedule> = emptyList()
)
