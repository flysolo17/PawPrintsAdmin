package com.eutech.pawprints.pets

import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.doctors.data.Doctors
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.utils.Results


data class PetState(
    val isLoading : Boolean = false,
    val pets : List<Pet> = emptyList(),
    val filteredPets : List<Pet> = emptyList(),
    val errors : String ? = null,
    val selectedSpecies : String = "all",
    val searchText : String = ""
)