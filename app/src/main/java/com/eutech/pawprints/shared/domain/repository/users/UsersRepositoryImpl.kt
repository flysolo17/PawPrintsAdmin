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

    override suspend fun getUserByID(
        userID: String,
        result: (Results<Users?>) -> Unit
    ) {
        result(Results.loading("Getting user with ID: $userID"))
        firestore.collection(USERS_COLLECTION)
            .document(userID)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(Users::class.java)
                    result(Results.success(user))
                } else {
                    result(Results.failuire("User with ID: $userID not found"))
                }
            }
            .addOnFailureListener { exception ->
                result(Results.failuire(exception.message.toString()))
                USERS_COLLECTION.createLog(
                    exception.message.toString(),
                    exception
                )
            }
    }

}