package com.eutech.pawprints.schedule.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.domain.ScheduleRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val doctorRepository: DoctorRepository,
)  : ViewModel() {
    var state by mutableStateOf(ScheduleState())
    init {
        events(ScheduleEvents.OnGetAllSchedules)
        events(ScheduleEvents.OnGetAllDoctors)
    }
    fun events(e : ScheduleEvents) {
        when(e ) {
            ScheduleEvents.OnGetAllSchedules -> getSchedules()
            ScheduleEvents.OnGetAllDoctors -> getDoctors()
          is  ScheduleEvents.OnCreateSchedule -> createSchedule(e.schedule)
            is ScheduleEvents.OnDelete -> deleteSchedule(e.id)
            is ScheduleEvents.OnAssignDoctor -> assign(e.id,e.doctorID)
        }
    }

    private fun assign(id: String, doctorID: String) {
        viewModelScope.launch {
            scheduleRepository.assignDoctor(id, doctorID = doctorID) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isDeleting = null,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isDeleting = id,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isDeleting = null,
                        errors = null,
                        deleted = it.data
                    )
                }
            }
            delay(1000)
            state = state.copy(deleted = null)
        }
    }

    private fun deleteSchedule(id: String) {
        viewModelScope.launch {
            scheduleRepository.deleteSchedule(id) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isDeleting = null,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isDeleting = id,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isDeleting = null,
                        errors = null,
                        deleted = it.data
                    )
                }
            }
            delay(1000)
            state = state.copy(deleted = null)
        }
    }

    private fun createSchedule(schedule: Schedule) {
        viewModelScope.launch {
            scheduleRepository.createSchedule(schedule) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isCreatingSchedule = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isCreatingSchedule = true,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isCreatingSchedule = false,
                        errors = null,
                        created = it.data
                    )
                }
            }
            delay(1000)
            state = state.copy(created = null)
        }
    }

    private fun getDoctors() {
        viewModelScope.launch {
            doctorRepository.getAllDoctors {
                if (it is Results.success) {
                    state = state.copy(
                        doctors = it.data
                    )
                }
                if (it is  Results.failuire) {
                    state = state.copy(
                        errors = it.message
                    )
                }
            }
        }
    }

    private fun getSchedules() {
        viewModelScope.launch {
            scheduleRepository.getDoctorSchedules {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null,
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        schedules = it.data
                    )
                }
            }
        }
    }

}