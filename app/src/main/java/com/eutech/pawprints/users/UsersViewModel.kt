package com.eutech.pawprints.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eutech.pawprints.shared.domain.repository.users.UsersRepository
import com.eutech.pawprints.shared.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {
    var state by mutableStateOf(UserState())
    init {
        events(UsersEvents.OnGetAllUsers)
    }
    fun events(e : UsersEvents) {
        when(e) {
            UsersEvents.OnGetAllUsers -> getUsers()
            is UsersEvents.OnSelectUser -> state = state.copy(selectedUser = e.users)
            is UsersEvents.OnSearch -> search(e.text)
        }
    }

    private fun search(text: String) {
        val filteredList = if (text.isBlank()) {
            state.users
        } else {
            state.users.filter { user ->
                user.name?.contains(text, ignoreCase = true) == true ||
                user.email?.contains(text,ignoreCase = true) == true ||
                user.phone?.contains(text,ignoreCase = true) == true
            }
        }

        state = state.copy(searchText = text, filteredUser = filteredList)
    }

    private fun getUsers() {
        viewModelScope.launch {
            usersRepository.getAllUsers {
                state = when(it) {
                    is Results.failuire -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is Results.loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is Results.success -> state.copy(
                        isLoading = false,
                        errors = null,
                        users = it.data,
                        filteredUser = it.data
                    )
                }
            }
        }
    }
}