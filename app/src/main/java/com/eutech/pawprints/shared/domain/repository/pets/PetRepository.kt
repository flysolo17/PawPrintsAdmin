package com.eutech.pawprints.shared.domain.repository.pets

import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.Results


interface PetRepository {
    suspend fun getAllPets(
        result: (Results<List<Pet>>) -> Unit
    )
    suspend fun getPetsByUserID(
        userID : String,
        result: (Results<List<Pet>>) -> Unit
    )
}