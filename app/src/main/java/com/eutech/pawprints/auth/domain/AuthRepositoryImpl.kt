package com.eutech.pawprints.auth.domain

import com.eutech.pawprints.auth.data.Administrator
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore
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
    companion object {
        const val ADMIN_COLLECTION = "administrator"
    }
}