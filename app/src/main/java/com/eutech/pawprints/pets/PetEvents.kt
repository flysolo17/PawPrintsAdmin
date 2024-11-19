package com.eutech.pawprints.pets

import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.Results


sealed interface PetEvents  {
    data object OnGetPets : PetEvents
    data class OnSearchPet(val text : String) : PetEvents
    data class OnSelectPet(val pet : Pet) : PetEvents
    data class OnGetPetAppointments(val petID : String) : PetEvents
}