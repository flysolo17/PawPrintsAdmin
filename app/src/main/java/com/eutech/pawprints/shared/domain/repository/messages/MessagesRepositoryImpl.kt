package com.eutech.pawprints.shared.domain.repository.messages

import com.eutech.pawprints.shared.data.messages.Message
import com.eutech.pawprints.shared.data.messages.UserWithMessages
import com.eutech.pawprints.shared.data.users.USERS_COLLECTION
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


const val MESSAGES_COLLECTION = "messages";
class MessagesRepositoryImpl(
    private val firestore: FirebaseFirestore
): MessagesRepository {
    override suspend fun getAllMessages(result: (Results<List<UserWithMessages>>) -> Unit) {
        result.invoke(Results.loading("Getting messages with users"))
        val users  : List<Users> = firestore.collection(USERS_COLLECTION)
            .get()
            .await()
            .toObjects(Users::class.java)

        firestore.collection(MESSAGES_COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    result.invoke(Results.failuire("Error fetching messages: ${error.message}"))
                    return@addSnapshotListener
                }
                snapshot?.let { querySnapshot ->
                    val messages = querySnapshot.toObjects(Message::class.java)
                    val userWithMessages = mutableListOf<UserWithMessages>()
                    users.forEach { user ->
                        val userMessages = messages.filter {
                            it.sender == user.id || it.receiver == user.id
                        }
                        userWithMessages.add(
                            UserWithMessages(
                                users = user,
                                messages = userMessages
                            )
                        )
                    }
                    userWithMessages.sortedByDescending {
                        it.messages.maxByOrNull { message -> message.createdAt }?.createdAt
                    }
                    result.invoke(Results.success(userWithMessages))
                }
            }
    }

    override suspend fun sendMessage(message: Message, result: (Results<String>) -> Unit) {
        result.invoke(Results.loading("Sending message"))
        firestore.collection(MESSAGES_COLLECTION)
            .document(message.id!!)
            .set(message)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result(Results.success("Message sent"))
                } else {
                    result(Results.failuire("error sending message"))
                }
            }.addOnFailureListener {
                result(Results.failuire(it.message.toString()))
            }

    }
}