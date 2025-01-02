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
            is PetEvents.OnSelectSpecies -> selectSpecies(e.species)
        }
    }

    private fun selectSpecies(species: String) {
        if (species == "all") {
            state = state.copy(
                filteredPets = state.pets,
                selectedSpecies = species
            )
        } else {
            val filteredPets = state.pets.filter {
                it.species.equals(species, ignoreCase = true)
            }
            state = state.copy(
                filteredPets = filteredPets,
                selectedSpecies = species
            )
        }
    }


    private fun search(text: String) {
        state = state.copy(
            selectedSpecies = "all"
        )
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