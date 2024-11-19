package com.eutech.pawprints.pets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.domain.AppointmentRepository
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
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        pets =  it.data,
                        filteredPets = it.data
                    )
                }
            }
        }
    }
}