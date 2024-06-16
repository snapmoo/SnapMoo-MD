package com.bangkit.snapmoo.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.snapmoo.data.pref.UserModel
import com.bangkit.snapmoo.data.repo.MainRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: MainRepository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}