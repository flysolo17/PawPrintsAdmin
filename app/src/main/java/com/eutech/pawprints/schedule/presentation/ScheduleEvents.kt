package com.eutech.pawprints.schedule.presentation

import com.eutech.pawprints.schedule.data.Schedule


sealed interface ScheduleEvents {
    data object OnGetAllSchedules : ScheduleEvents
    data object OnGetAllDoctors : ScheduleEvents
    data class OnCreateSchedule(
        val schedule: Schedule
    ) : ScheduleEvents
    data class OnDelete(val id : String) : ScheduleEvents
    data class OnAssignDoctor(val id :String,val doctorID : String) :ScheduleEvents
}