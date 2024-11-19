package com.eutech.pawprints.shared.domain.repository.users

import com.eutech.pawprints.shared.data.users.USERS_COLLECTION
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.presentation.utils.Results
import com.eutech.pawprints.ui.custom.createLog
import com.google.firebase.firestore.FirebaseFirestore

class UsersRepositoryImpl(
    private val firestore: FirebaseFirestore
): UsersRepository {
    override suspend fun getAllUsers(result: (Results<List<Users>>) -> Unit) {
        result(Results.loading("Getting all users"))
        firestore.collection(USERS_COLLECTION)
            .addSnapshotListener { value, error ->
                value?.let {
                    result(Results.success(it.toObjects(Users::class.java)))
                }
                error?.let {
                    result(Results.failuire(it.message.toString()))
                    USERS_COLLECTION.createLog(
                        it.message.toString(),
                        it
                    )
                }
            }
    }
}