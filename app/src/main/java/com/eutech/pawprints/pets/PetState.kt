package com.eutech.pawprints.pets

import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.utils.Results


data class PetState(
    val isLoading : Boolean = false,
    val pets : List<Pet> = emptyList(),
    val filteredPets : List<Pet> = emptyList(),
    val errors : String ? = null,
    val selectedPet : Pet? = null,
    val searchText : String = "",


    val isGettingPetSchedule : Boolean = false,
    val selectedPetAppointments : List<Appointments> = emptyList(),
    val petAppointmentError : String ? = null
)