package com.eutech.pawprints.shared.presentation.utils

sealed class Results<out T> {
    data class loading(val title : String) : Results<Nothing>()
    data class failuire(val message: String) : Results<Nothing>()
    data class success<T>(val data : T) : Results<T>()
}