package com.eutech.pawprints.shared.data.transactions

import com.eutech.pawprints.shared.data.users.Users


data class TransactionWithUser(
    val transaction: Transaction,
    val users: Users ? = null,
)