package com.eutech.pawprints.shared.domain.repository.inbox

import com.eutech.pawprints.appointments.data.appointment.Inbox
import com.eutech.pawprints.appointments.domain.INBOX_COLLECTION
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.ui.custom.createLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class InboxRepositoryImpl(
    private val firestore : FirebaseFirestore,
): InboxRepository {
    override suspend fun getAllInboxByUserID(
        userID: String,
        result: (Results<List<Inbox>>) -> Unit
    ) {
        firestore.collection(INBOX_COLLECTION)
            .whereEqualTo("userID",userID)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                INBOX_COLLECTION.createLog(error?.message.toString(),error)
                value?.let {
                    result.invoke(Results.success(it.toObjects(Inbox::class.java)))
                }
                error?.let {
                    result.invoke(Results.failuire(it.message.toString()))
                }
            }
    }
}