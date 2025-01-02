package com.eutech.pawprints.shared.data.pets

import com.eutech.pawprints.shared.data.users.Users


data class PetWithOwner(
    val pet: Pet ? = null,
    val owner : Users ? = null,
)