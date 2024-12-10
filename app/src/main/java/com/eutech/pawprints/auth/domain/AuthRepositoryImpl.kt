package com.eutech.pawprints.auth.domain

import android.net.Uri
import com.eutech.pawprints.auth.data.Administrator
import com.eutech.pawprints.products.domain.ProductRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage : FirebaseStorage
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String,
        result: (Results<FirebaseUser>) -> Unit
    ) {
        try {
            result.invoke(Results.loading("Logging in..."))
            val loginResult = auth.signInWithEmailAndPassword(email,password).await()
            if (loginResult.user == null) {
                result.invoke(Results.failuire("User not found!"))
                return
            }
            result.invoke(Results.success(loginResult.user!!))
        }catch (e : Exception) {
            result.invoke(Results.failuire(e.localizedMessage?:"Unknown Error"))
        }
    }

    override fun getAdminData( result: (Results<Administrator?>) -> Unit) {
        result.invoke(Results.loading("Getting admin data..."))
        val user = auth.currentUser
        if (user == null) {
            result.invoke(Results.failuire("No user found!"))
            return
        }
        firestore
            .collection(ADMIN_COLLECTION)
            .document(user.uid)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(Results.failuire(it.localizedMessage?: "unknown error"))
                }
                value?.let {
                    val admin = it.toObject(Administrator::class.java)
                    if (admin == null) {
                        result.invoke(Results.failuire("Cannot find admin"))
                        return@let
                    }
                    result.invoke(Results.success(admin))
                }
            }
    }

    override suspend fun register() {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(id: String, uri: Uri, result: (Results<String>) -> Unit) {
        val storageRef = storage.reference.child(
            "${ADMIN_COLLECTION}/${id}/${uri.lastPathSegment}"
        )
        val uploadTask = storageRef.putFile(uri).await()
        val downloadUrl =storageRef.downloadUrl.await()

        firestore.collection(ADMIN_COLLECTION)
            .document(id)
            .update(
                "profile",downloadUrl
            )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result(Results.success("Successfully Updated!"))
                } else {
                    result(Results.failuire("Error updating profile"))
                }
            }.addOnFailureListener {
                result(Results.failuire(it.message.toString()))
            }
    }

    override suspend fun editProfile(
        id: String,
        name: String,
        phone: String,
        email: String,
        result: (Results<String>) -> Unit
    ) {
        try {
            result(Results.loading("Edit Profile"))


            val updateData = mapOf(
                "name" to name,
                "phone" to phone,
                "email" to email
            )
            val userDocument = firestore.collection(ADMIN_COLLECTION).document(id)

            userDocument.update(updateData)
                .addOnSuccessListener {
                    result(Results.success("Profile updated successfully"))
                }
                .addOnFailureListener { exception ->
                    result(Results.failuire(exception.localizedMessage ?: "Error updating profile"))
                }

        } catch (e: Exception) {
            result(Results.failuire(e.localizedMessage ?: "Unexpected error occurred"))
        }
    }

    companion object {
        const val ADMIN_COLLECTION = "administrator"
    }
}