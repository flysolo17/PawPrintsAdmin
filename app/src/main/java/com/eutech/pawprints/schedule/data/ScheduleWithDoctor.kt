package com.eutech.pawprints.schedule.data

import com.eutech.pawprints.doctors.data.Doctors


data class ScheduleWithDoctor(
    val schedule: Schedule? = null,
    val doctors: Doctors ? = null,
)