package com.eutech.pawprints.auth.domain

import android.net.Uri
import com.eutech.pawprints.auth.data.Administrator
import com.eutech.pawprints.shared.presentation.utils.Results
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow

interface AuthRepository {

    suspend fun login( email : String, password : String,result : (Results<FirebaseUser>) -> Unit)
    fun getAdminData(result: (Results<Administrator?>) -> Unit)
    suspend fun register()
    suspend fun updateProfile(
        id : String ,
        uri : Uri,
        result: (Results<String>) -> Unit
    )

    suspend fun editProfile(
        id : String,
        name : String,
        phone : String,
        email : String,
        result: (Results<String>) -> Unit
    )
}