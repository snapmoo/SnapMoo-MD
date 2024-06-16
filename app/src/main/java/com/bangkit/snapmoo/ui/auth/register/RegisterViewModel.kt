package com.bangkit.snapmoo.ui.auth.register

import androidx.lifecycle.ViewModel
import com.bangkit.snapmoo.data.repo.MainRepository

class RegisterViewModel(private val repository: MainRepository) : ViewModel() {

    fun register(name: String, email: String, password: String, phoneNumber: String) =
        repository.register(name, email, password, phoneNumber)

}