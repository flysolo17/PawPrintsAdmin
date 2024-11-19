package com.eutech.pawprints.schedule.domain

import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.data.ScheduleWithDoctor
import com.eutech.pawprints.shared.presentation.utils.Results
import org.threeten.bp.LocalDate

interface ScheduleRepository {
    suspend fun createSchedule(
        schedule: Schedule,
        result : (Results<String> ) -> Unit
    )

    suspend fun getDoctorSchedules(
        result: (Results<List<Schedule>>) -> Unit
    )
    suspend fun deleteSchedule(
        id : String,
        result: (Results<String>) -> Unit
    )

    suspend fun assignDoctor(
        id : String,
        doctorID : String,
        result: (Results<String>) -> Unit
    )
    suspend fun getScheduleByMonth(
        localDate: LocalDate,
        result: (Results<List<ScheduleWithDoctor>>) -> Unit
    )
}