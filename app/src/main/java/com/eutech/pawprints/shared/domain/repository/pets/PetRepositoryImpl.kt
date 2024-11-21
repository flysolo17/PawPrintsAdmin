package com.eutech.pawprints.shared.domain.repository.pets

import com.eutech.pawprints.shared.data.pets.PETS_COLLECTION
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay

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

    override suspend fun getPetsByUserID(userID: String, result: (Results<List<Pet>>) -> Unit) {
        result.invoke(Results.loading("Getting my petss"))
        delay(1000)
        firestore.collection(PETS_COLLECTION)
            .whereEqualTo("ownerID", userID)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result(Results.success(it.result.toObjects(Pet::class.java)))
                } else {
                    result(Results.failuire("Error getting pets"))
                }
            }.addOnFailureListener {
                result(Results.failuire(it.message.toString()))
            }
    }
}