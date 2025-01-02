package com.eutech.pawprints.pets.view_pets

import android.net.Uri
import com.eutech.pawprints.pets.PetEvents
import com.eutech.pawprints.shared.data.medical.MedicalRecord


sealed interface ViewPetEvents {
    data class OnGetPetInfo(val id : String) : ViewPetEvents
    data class OnAddInfo(val petID : String,val label : String,val value : String) : ViewPetEvents
    data object OnGetAllDoctors : ViewPetEvents

    data class OnGetPetAppointments(val petID : String) : ViewPetEvents



    data class OnSavemedicalRecord(
        val petID: String,
        val record: MedicalRecord,
        val images : List<Uri>
    ) : ViewPetEvents

    data class OngetMedicalRecord(
        val  petID: String
    ) : ViewPetEvents

    data class OnDeletePet(val id : String) : ViewPetEvents
}