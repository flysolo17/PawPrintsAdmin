package com.eutech.pawprints.pets

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.domain.AppointmentRepository
import com.eutech.pawprints.doctors.domain.DoctorRepository
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.pets.Details
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.domain.repository.pets.PetRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository : PetRepository,
    private val appointmentRepository: AppointmentRepository,
    private val doctorRepository: DoctorRepository
) : ViewModel() {
    var state by mutableStateOf(PetState())
    init {
        events(PetEvents.OnGetPets)

    }
    fun events(e : PetEvents) {
        when(e) {
            PetEvents.OnGetPets -> getPets()
            is PetEvents.OnSearchPet -> search(text = e.text)
            is PetEvents.OnSelectPet -> selectPet(e.pet)
            is PetEvents.OnGetPetAppointments -> getPetAppointments(e.petID)
            is PetEvents.OnAddInfo -> addInfo(e.petID,e.label,e.value)
            PetEvents.OnGetAllDoctors -> getDoctors()
            is PetEvents.OnSavemedicalRecord -> addmedicalRecord(
                e.petID,
                e.record,
                e.images
            )

            is PetEvents.OngetMedicalRecord ->getRecords(e.petID)
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
                    medicalRecords = recordsWithDoctors
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
                    isAddingInfo = false
                )
            }.onFailure {
                state = state.copy(
                    isAddingInfo = false
                )
            }
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
            }
        }

    }

    private fun addInfo(petID: String, label: String, value: String) {
        viewModelScope.launch {
            val data = Details(
                label = label,
                value = value
            )
            state = state.copy(
                isAddingInfo = true
            )
            petRepository.addPetInfo(
                petID,
                data
            ).onSuccess {
                state = state.copy(
                    isAddingInfo = false,
                    selectedPet = state.selectedPet?.copy( otherDetails = state.selectedPet?.otherDetails.orEmpty() + it )
                )
            }.onFailure {
               state = state.copy(
                   isAddingInfo = false,
                    errors = it.localizedMessage?.toString()
                )
            }
        }
    }

    private fun selectPet(pet: Pet) {
        state = state.copy(selectedPet = pet)
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
                        selectedPetAppointments = it.data
                    )
                }
            }
        }
    }


    private fun search(text: String) {
        val filteredList = if (text.isBlank()) {
            state.pets
        } else {
            state.pets.filter { pet ->
                pet.name?.contains(text, ignoreCase = true) == true
            }
        }

        state = state.copy(searchText = text, filteredPets = filteredList)
    }

    private fun getPets() {
        viewModelScope.launch {
            petRepository.getAllPets {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> {
                        events(PetEvents.OnGetAllDoctors)
                        state.copy(
                            isLoading = false,
                            errors = null,
                            pets = it.data,
                            filteredPets = it.data
                        )
                    }
                }
            }
        }
    }
}