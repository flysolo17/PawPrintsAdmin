package com.eutech.pawprints.shared.data.messages

import java.util.Date

data class Message(
    val id : String ? = null,
    val message: String  ?= null,
    val sender : String ? = null,
    val receiver : String  = "7ter7VWCmbUzZLe51b8wOraZgEy2",
    val type: UserType = UserType.CLIENT,
    val createdAt : Date = Date(),
    val seen : Boolean = false
)

enum class UserType {
    ADMIN,
    CLIENT
}