package com.example.ecoplay_front.viewModel

import androidx.lifecycle.ViewModel
import com.example.ecoplay_front.model.UserModel
import com.example.ecoplay_front.repository.UserRepository

class UserViewModel (private val repository: UserRepository) : ViewModel() {

    fun getUsers(callback: (List<UserModel>?) -> Unit) {
        repository.getUsers(callback)
    }
}