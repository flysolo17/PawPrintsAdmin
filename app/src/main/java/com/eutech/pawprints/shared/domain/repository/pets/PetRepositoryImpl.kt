package com.eutech.pawprints.shared.domain.repository.pets

import android.net.Uri
import com.eutech.pawprints.shared.data.medical.MedicalRecord
import com.eutech.pawprints.shared.data.medical.MedicalRecordWithDoctor
import com.eutech.pawprints.shared.data.pets.Details
import com.eutech.pawprints.shared.data.pets.PETS_COLLECTION
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.util.Date


const val MEDICAL_RECORD_COLLECTION = "medical_records"
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

    override suspend fun addPetInfo(petID: String, data:Details): Result<Details> {
        return try {
            firestore.collection(PETS_COLLECTION)
                .document(petID)
                .update(
                    "otherDetails",FieldValue.arrayUnion(data),
                    "updatedAt",Date()
                ).await()
            Result.success(data)
        } catch (e : Exception) {
            Result.failure(e)
        }


    }

    override suspend fun addMedicalInfo(
        petID: String,
        record: MedicalRecord,
        images: List<Uri>
    ): Result<MedicalRecord> {
        val medicalRef = firestore.collection(MEDICAL_RECORD_COLLECTION)
        val storageRef = storage.reference.child("$MEDICAL_RECORD_COLLECTION/$petID/${record.id}")

        return try {
            val uploadedImageUrls = images.mapIndexed { index, uri ->
                val imageRef = storageRef.child("image_$index.jpg")
                val uploadTask = imageRef.putFile(uri).await()
                imageRef.downloadUrl.await().toString()
            }
            val updatedRecord = record.copy(images = uploadedImageUrls)
            medicalRef.document(record.id).set(updatedRecord).await()
            Result.success(updatedRecord)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllMedicalRecordWithDoctor(petID: String): Result<List<MedicalRecord>> {
        val medicalRef = firestore.collection(MEDICAL_RECORD_COLLECTION)

        return try {
            // Fetch all medical records for the given petID
            val medicalRecordsSnapshot = medicalRef.whereEqualTo("petID", petID).get().await()

            // Map Firestore documents to MedicalRecord objects
            val medicalRecords = medicalRecordsSnapshot.documents.mapNotNull { document ->
                document.toObject(MedicalRecord::class.java)
            }

            Result.success(medicalRecords)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}