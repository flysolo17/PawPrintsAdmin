package com.eutech.pawprints.doctors.presentation.view_doctor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.AMPMHours
import com.eutech.pawprints.schedule.data.Schedule
import com.eutech.pawprints.schedule.data.toPawPrintTime
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.schedule.domain.ScheduleRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.shared.presentation.utils.generateRandownString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class ViewDoctorViewModel @Inject constructor(
     private val doctorRepository: DoctorRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    var state by mutableStateOf(ViewDoctorState())


    @RequiresApi(Build.VERSION_CODES.O)
    fun events(e : ViewDoctorEvents) {
        when(e) {

            is ViewDoctorEvents.OnGetDoctorInfo -> getDoctorInfo(e.doctorID)

        }
    }

    private fun getDoctorInfo(doctorID: String) {
        viewModelScope.launch {
            doctorRepository.getDoctorById(doctorID) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        doctor = it.data
                    )
                }
            }
        }
    }

}