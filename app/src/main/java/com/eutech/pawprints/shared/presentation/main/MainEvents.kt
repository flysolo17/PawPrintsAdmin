package com.eutech.pawprints.shared.presentation.main




sealed interface MainEvents {
    data object OnGetUnseenMessages : MainEvents

}