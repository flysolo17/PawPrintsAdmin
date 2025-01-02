package com.eutech.pawprints.pets.view_pets

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.appointments.domain.AppointmentRepository
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.pets.Details
import com.eutech.pawprints.shared.domain.repository.pets.PetRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.core.View
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewPetViewModel @Inject constructor(
    private val petRepository : PetRepository,
    private  val doctorRepository: DoctorRepository,
    private  val appointmentRepository : AppointmentRepository
) : ViewModel() {
    var state by mutableStateOf(ViewPetState())
    init {
        events(ViewPetEvents.OnGetAllDoctors)
    }
    fun events(e : ViewPetEvents) {
        when(e) {
            is ViewPetEvents.OnGetPetInfo -> getPetInfo(e.id)
            is ViewPetEvents.OnAddInfo ->addInfo(e.petID,e.label,e.value)
            ViewPetEvents.OnGetAllDoctors -> getAllDoctors()
            is ViewPetEvents.OnGetPetAppointments -> getPetAppointments(e.petID)
            is ViewPetEvents.OnSavemedicalRecord ->addmedicalRecord(
                e.petID,
                e.record,
                e.images
            )
            is ViewPetEvents.OngetMedicalRecord ->getRecords(e.petID)
            is ViewPetEvents.OnDeletePet -> deletePet(e.id)
        }
    }

    private fun deletePet(id: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            petRepository.deletePet(id).onSuccess {
                state = state.copy(
                    isLoading = false,
                    isPetDeleted = it
                )
            }.onFailure {
                state = state.copy(
                    isLoading = false,
                    errors = it.localizedMessage.toString()
                )
            }
        }
    }

    private fun getRecords(petID: String) {
        viewModelScope.launch {
            state = state.copy(
                isGettingMedicalRecord =true
            )
            val result = petRepository.getAllMedicalRecordWithDoctor(petID)
            if (result.isSuccess) {
                val records = result.getOrNull() ?: emptyList()
                val doctors = state.doctors

                // Map each medical record with its associated doctor
                val recordsWithDoctors = records.map { medicalRecord ->
                    val doctor = doctors.find { it.id == medicalRecord.doctorID }
                    MedicalRecordWithDoctor(
                        record = medicalRecord,
                        doctors = doctor
                    )
                }

                // Update state with records and associated doctors
                state= state.copy(
                    isGettingMedicalRecord = false,
                    records = recordsWithDoctors
                )
            } else {
                val error = result.exceptionOrNull()
                state = state.copy(
                    isGettingMedicalRecord = false,
                    errors = error?.message?:""
                )
            }
        }
    }



    private fun addmedicalRecord(petID: String, record: MedicalRecord, images: List<Uri>) {
        viewModelScope.launch {
            state = state.copy(
                isAddingInfo = true
            )
            petRepository.addMedicalInfo(
                petID,
                record, images
            ).onSuccess {
                state = state.copy(
                    isAddingInfo = false,
                )
                events(ViewPetEvents.OngetMedicalRecord(petID))
            }.onFailure {
                state = state.copy(
                    isAddingInfo = false
                )
            }
        }
    }


    private fun getPetAppointments(petID: String) {
        viewModelScope.launch {
            appointmentRepository.getAppointmentByPetID(petID) {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isGettingPetSchedule = false,
                        petAppointmentError = it.message,
                    )
                    is Results.loading -> state.copy(
                        isGettingPetSchedule = true,
                        petAppointmentError = null
                    )
                    is Results.success -> state.copy(
                        isGettingPetSchedule = false,
                        petAppointmentError = null,
                        appointments = it.data
                    )
                }
            }
        }
    }


    private fun getAllDoctors() {
        viewModelScope.launch {
            doctorRepository.getAllDoctors {
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
                        doctors = it.data
                    )
                }
            }
        }
    }

    private fun addInfo(petID: String, label: String, value: String) {
        viewModelScope.launch {
            val data = Details(
                label = label,
                value = value
            )

            state = state.copy(isAddingInfo = true)

            // Perform the repository operation
            petRepository.addPetInfo(petID, data)
                .onSuccess { updatedDetail ->
                    val updatedPetWithOwner = state.petWithOwner?.let { petWithOwner ->
                        petWithOwner.copy(
                            pet = petWithOwner.pet?.copy(
                                otherDetails = petWithOwner.pet.otherDetails.orEmpty() + updatedDetail
                            )
                        )
                    }

                    state = state.copy(
                        isAddingInfo = false,
                        petWithOwner = updatedPetWithOwner
                    )
                }
                .onFailure { exception ->
                    state = state.copy(
                        isAddingInfo = false,
                        errors = exception.localizedMessage ?: "An unknown error occurred"
                    )
                }
        }
    }


    private fun getPetInfo(id: String) {
        viewModelScope.launch {
            petRepository.getPetInfoWithOwner(id) {
             when(it) {
                    is Results.failuire ->state =  state.copy(
                        isLoading = false,
                        errors = it.message
                    )

                    is Results.loading ->state =  state.copy(
                        isLoading = true,
                        errors = null
                    )

                    is Results.success -> {
                      state =  state.copy(
                            isLoading = false,
                            errors = null,
                            petWithOwner = it.data
                      )
                        events(ViewPetEvents.OnGetAllDoctors)
                    }
                }
            }
        }
    }
}