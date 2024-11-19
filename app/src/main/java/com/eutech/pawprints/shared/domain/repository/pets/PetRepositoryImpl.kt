package com.eutech.pawprints.shared.domain.repository.pets

import com.eutech.pawprints.shared.data.pets.PETS_COLLECTION
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PetRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage : FirebaseStorage
): PetRepository {
    override suspend fun getAllPets(result: (Results<List<Pet>>) -> Unit) {
        result.invoke(Results.loading("Getting All Pets"))
        firestore.collection(PETS_COLLECTION)
            .addSnapshotListener { value, error ->
                value?.let {
                    result(Results.success(it.toObjects(Pet::class.java)))
                }
                error?.let {
                    result(Results.failuire(it.message.toString()))
                }
            }
    }
}