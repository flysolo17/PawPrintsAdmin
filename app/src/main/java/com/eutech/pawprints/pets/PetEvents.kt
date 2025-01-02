package com.eutech.pawprints.pets

import android.net.Uri
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.Results


sealed interface PetEvents  {
    data object OnGetPets : PetEvents
    data class OnSearchPet(val text : String) : PetEvents
    data class OnSelectSpecies(val species : String) : PetEvents
}