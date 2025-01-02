package com.eutech.pawprints.shared.domain.repository.pets

import android.net.Uri
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.pets.Details
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.data.pets.PetWithOwner
import com.eutech.pawprints.shared.presentation.utils.Results


interface PetRepository {
    suspend fun getAllPets(
        result: (Results<List<Pet>>) -> Unit
    )

    suspend fun getPetsByUserID(
        userID: String,
        result: (Results<List<Pet>>) -> Unit
    )

    suspend fun addPetInfo(
        petID: String,
        data: Details,
    ): Result<Details>


    suspend fun addMedicalInfo(
        petID: String,
        record: MedicalRecord,
        images: List<Uri>,
    ): Result<MedicalRecord>

    suspend fun getAllMedicalRecordWithDoctor(
        petID: String
    ): Result<List<MedicalRecord>>

    suspend fun getPetInfoWithOwner(
        petID: String,
        result: (Results<PetWithOwner>) -> Unit
    )

    suspend fun deletePet(
        id : String
    ) : Result<String>
}
