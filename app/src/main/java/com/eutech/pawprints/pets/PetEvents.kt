package com.eutech.pawprints.pets

import android.net.Uri
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.Results


sealed interface PetEvents  {
    data object OnGetPets : PetEvents
    data class OnSearchPet(val text : String) : PetEvents
    data class OnSelectPet(val pet : Pet) : PetEvents
    data class OnGetPetAppointments(val petID : String) : PetEvents

    data class OnAddInfo(val petID : String,val label : String,val value : String) : PetEvents
    data object OnGetAllDoctors : PetEvents
    data class OnSavemedicalRecord(
        val petID: String,
        val record: MedicalRecord,
        val images : List<Uri>
    ) : PetEvents

    data class OngetMedicalRecord(
       val  petID: String
    ) : PetEvents
}