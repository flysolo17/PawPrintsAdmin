package com.eutech.pawprints.shared.domain.repository.users

import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.presentation.utils.Results

interface UsersRepository {

    suspend fun getAllUsers(result : (Results<List<Users>>) -> Unit)
}