package com.eutech.pawprints.auth.presentation.profile




sealed interface ProfileEvents {
    data object OnGetAdminInfo : ProfileEvents
}