package com.eutech.pawprints.shared.domain.repository.inbox

import com.eutech.pawprints.appointments.data.appointment.Inbox
import com.eutech.pawprints.appointments.domain.INBOX_COLLECTION
import com.eutech.pawprints.shared.data.pets.Pet
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.ui.custom.createLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay

class InboxRepositoryImpl(
    private val firestore : FirebaseFirestore,
): InboxRepository {
    override suspend fun getAllInboxByUserID(
        userID: String,
        result: (Results<List<Inbox>>) -> Unit
    ) {
        result(Results.loading("Getting user inbox"))
        delay(1000)
        firestore.collection(INBOX_COLLECTION)
            .whereEqualTo("userID",userID)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result(Results.success(it.result.toObjects(Inbox::class.java)))
                } else {
                    result(Results.failuire("Error getting inboxes"))
                }
            }.addOnFailureListener {
                result(Results.failuire(it.message.toString()))
            }
    }

    override suspend fun getInboxByCollectionID(
        collectionID: String,
        result: (Results<List<Inbox>>) -> Unit
    ) {
        result(Results.loading("Getting inbox for collection ID: $collectionID"))
        firestore.collection(INBOX_COLLECTION)
            .whereEqualTo("collectionID", collectionID)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val inboxList = querySnapshot.toObjects(Inbox::class.java)
                result(Results.success(inboxList))
            }
            .addOnFailureListener { exception ->
                result(Results.failuire(exception.message.toString()))
                INBOX_COLLECTION.createLog(
                    exception.message.toString(),
                    exception
                )
            }
    }

}