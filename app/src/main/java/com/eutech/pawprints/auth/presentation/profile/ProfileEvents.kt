package com.eutech.pawprints.auth.presentation.profile

import android.net.Uri


sealed interface ProfileEvents {
    data object OnGetAdminInfo : ProfileEvents
    data class OnProfileChange(val uri : Uri ) : ProfileEvents

    data class OnUpdate(
        val id : String,
        val name : String,
        val phone : String,
        val email : String
    ) : ProfileEvents
}